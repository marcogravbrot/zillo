import Classes.Connection;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;

public class Core {
    public static void message(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) throws URISyntaxException {
        System.out.println("Zillo initialized");

        Connection connection = new Connection(new URI("wss://zillo.gravbrot.it"));
        connection.connect();

        JFrame m = new JFrame();
        //message("halla", "balla");
    }
}