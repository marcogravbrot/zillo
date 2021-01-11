package Classes;

import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InstructionResolver {
    Gson json = new Gson();
    private String InstructionsPath = "src/Classes/Instructions";
    List<Class> instructions = new ArrayList<>();

    public InstructionResolver() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        File file = new File(InstructionsPath);
        File[] files = file.listFiles();

        if (files != null) {
            for (File f : files) {
                String fname = f.getName();

                if (fname.endsWith(".java")) {
                    Class instruction = Class.forName("Classes.Instructions." + fname.substring(0, fname.length() - 5));
                    instructions.add(instruction);

                    String type = (String) instruction.getField("type").get("type");

                    //System.out.println(type);
                }
            }
        }
    }

    public void HandleInstruction(String instruction, String from, Connection connection) throws NoSuchFieldException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        HashMap<String, String> command = json.fromJson(instruction, HashMap.class);

        String type = command.get("type");

        for (Class instructionClass : instructions) {
            String instructionType = (String) instructionClass.getField("type").get("type");

            if (type.equals(instructionType)) {
                System.out.println("Found a noice thing");

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

