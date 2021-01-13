import Classes.Connection;
import Classes.Initialize;
import Classes.Instruction;
import Classes.InstructionResolver;
import Classes.Instructions.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

public class Core {
    public static void main(String[] args) throws URISyntaxException, IOException {
        System.out.println("zillo initialized");

        Connection connection = new Connection(new URI("wss://zillo.gravbrot.it"));
        connection.connect();

//        String path = Core.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String decodedPath = URLDecoder.decode(path, "UTF-8");
//
//        System.out.println(path);
//        System.out.println(Thread.currentThread().getStackTrace()[1]);

//        final File jarFile = new File(Core.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//        System.out.println("JARfile" + jarFile);
//
//        URL url = Core.class.getResource("Classes/Instructions/Exec");
//        System.out.println(url);
//
//        url = Core.class.getResource("Classes/Instructions/Exec.class");
//        System.out.println(url);

//        URI uri = Core.class.getResource("/").toURI();
//        Path myPath;
//        if (uri.getScheme().equals("jar")) {
//            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
//            myPath = fileSystem.getPath("/");
//        } else {
//            myPath = Paths.get(uri);
//        }
//        Stream<Path> walk = Files.walk(myPath, 1);
//        for (Iterator<Path> it = walk.iterator(); it.hasNext();){
//            System.out.println(it.next());
//        }
    }
}