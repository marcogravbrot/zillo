package Classes.Instructions;

import Classes.Connection;
import Classes.Instruction;

import javax.swing.*;
import java.util.HashMap;

public class Message extends Instruction {
    public static String type = "message";

    public Message(Connection connection) {
        super(connection, type);
    }

    public void execute(HashMap<String, Object> arguments, String from) {
        System.out.println(arguments);

        String message = (String) arguments.get("message");
        String title = (String) arguments.get("title");

        System.out.println(message);
        System.out.println(title);

        JFrame m = new JFrame();
        JOptionPane.showMessageDialog(m, "mhhhm");
    }
}