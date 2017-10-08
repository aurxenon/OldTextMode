package net.edcubed.TextMode;

import com.googlecode.lanterna.terminal.Terminal;
import net.edcubed.GraphicalStuff.Display;
import net.edcubed.InputStuff.KeyboardManager;
import net.edcubed.NetworkStuff.NetworkManager;

public class Globals {
    public static Terminal terminal;
    public static NetworkManager networkManager = new NetworkManager("localhost",26656,26655);
    public static KeyboardManager keyboardManager;
    public static Display display;
}
