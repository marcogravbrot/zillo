import Classes.Connection;

import java.net.URI;
import java.net.URISyntaxException;

public class Core {
    public static void main(String[] args) throws URISyntaxException {
        System.out.println("Zillo initialized");

        Connection connection = new Connection(new URI("wss://zillo.gravbrot.it"));
        connection.connect();
    }
}