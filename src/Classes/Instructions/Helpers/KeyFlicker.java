package Classes.Instructions.Helpers;

import Classes.Instructions.KeyFlick;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyFlicker implements NativeKeyListener {
    public static boolean enabled = true;

    public void nativeKeyPressed(NativeKeyEvent e) {
        String typedCharacter = NativeKeyEvent.getKeyText(e.getKeyCode());

        System.out.println("Key Pressed: " + typedCharacter);

        KeyFlick.typed.add(typedCharacter);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        System.out.println("key typed??");
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}
