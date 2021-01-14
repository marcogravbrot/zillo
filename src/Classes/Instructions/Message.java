package Classes.Instructions;

import Classes.Connection;
import Classes.Instruction;
import Classes.Instructions.Helpers.MessageBox;

import java.util.HashMap;

public class Message extends Instruction {
    public static String type = "message";
    private MessageBox messageBox = new MessageBox();

    public Message(Connection connection) {
        super(connection, type);
    }

    public void execute(HashMap<String, Object> arguments, String from) {
        String message = (String) arguments.get("message");
        String title = (String) arguments.get("title");

        MessageBox.show(message, title);
    }
}