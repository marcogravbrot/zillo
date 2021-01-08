import Classes.Connection;
import Classes.Instructions.Exec;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.*;

public class Core {
    public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException {
        System.out.println("zillo initialized");

        Connection connection = new Connection(new URI("wss://zillo.gravbrot.it"));
        connection.connect();

//        String path = Core.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String decodedPath = URLDecoder.decode(path, "UTF-8");
//
//        System.out.println(path);
//        System.out.println(Thread.currentThread().getStackTrace()[1]);
    }
}