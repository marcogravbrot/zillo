package Classes;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import Classes.Instructions.*;



public class InstructionResolver {
    Gson json = new Gson();
    private String InstructionsPath = "src/Classes/Instructions";
    List<Class> instructions = new ArrayList<>();

    public InstructionResolver() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, URISyntaxException, IOException {
        URI uri = InstructionResolver.class.getResource("Instructions/").toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath("/Classes/Instructions");
        } else {
            myPath = Paths.get(uri);
        }
        Stream<Path> walk = Files.walk(myPath, 1);
        for (Iterator<Path> it = walk.iterator(); it.hasNext();){
            String filename = it.next().getFileName().toString();

            if (filename.toString().endsWith(".class")) {
                filename = filename.substring(0, filename.lastIndexOf('.'));

                Class instruction = Class.forName("Classes.Instructions." + filename);
                instructions.add(instruction);

                String type = (String) instruction.getField("type").get("type");
            }
        }
    }

    public void HandleInstruction(String instruction, String from, Connection connection) throws NoSuchFieldException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        HashMap<String, String> command = json.fromJson(instruction, HashMap.class);

        String type = command.get("type");

        for (Class instructionClass : instructions) {
            String instructionType = (String) instructionClass.getField("type").get("type");

            if (type.equals(instructionType)) {
                HashMap<String, Object> arguments = new HashMap<String, Object>();

                for (String key : command.keySet()) {
                    if (key.equals("type")) {
                        continue;
                    }

                    arguments.put(key, command.get(key));
                }

                Object cons = instructionClass.getConstructor(Connection.class).newInstance(connection);
                Method method = cons.getClass().getMethod("execute", HashMap.class, String.class);

                method.invoke(cons, arguments, from);

                break;
            }
        }
    }
}

