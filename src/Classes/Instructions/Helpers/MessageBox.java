package Classes.Instructions.Helpers;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

interface User32 extends StdCallLibrary {
    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
    int MessageBoxA(int hwnd, String text, String caption, int type);
}

public class MessageBox {
    public static void show(String infoMessage, String titleBar)
    {
        User32 user32 = User32.INSTANCE;

        user32.MessageBoxA(0, infoMessage, titleBar, 0x00001030);
    }
}
