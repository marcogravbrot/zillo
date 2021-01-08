package Classes;

import Classes.Instructions.Exec;
import com.google.gson.Gson;

import java.io.IOException;

public class Instruction {
    public String type;
    public String[] values;
    public Connection connection;
    public Gson json = new Gson();

    public Instruction(Connection connection, String type, String[] values) {
        this.type = type;
        this.values = values;
        this.connection = connection;
    }

    public void execute() throws IOException {
        System.out.println("Executing command " + type);
    }
}

