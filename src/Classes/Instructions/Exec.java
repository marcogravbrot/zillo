package Classes.Instructions;
import Classes.Connection;
import Classes.Instruction;

public class Exec extends Instruction {
    public Exec(Connection connection, String[] values) {
        super(connection, "exec", values);
    }

    @Override
    public void execute() {
        System.out.println(this.type);
    }
}