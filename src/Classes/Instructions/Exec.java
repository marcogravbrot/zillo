package Classes.Instructions;
import Classes.Connection;
import Classes.Instruction;

import java.io.IOException;
import java.util.HashMap;

public class Exec extends Instruction {
    public static String type = "exec";

    public Exec(Connection connection) {
        super(connection, type);
    }

    public void execute(HashMap<String, Object> arguments, String from) {
        String exec = (String) arguments.get("value");

        System.out.println("Running command " + exec);

        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(exec);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}