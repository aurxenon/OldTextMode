package net.edcubed.InputStuff;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import net.edcubed.NetworkStuff.NetworkListener;
import net.edcubed.TextModeCommons.PlayerMP;
import net.edcubed.TextMode.*;

import java.io.IOException;
import java.util.ArrayList;

public class KeyboardManager {
    //listeners
    private ArrayList<KeyboardListener> listeners = new ArrayList<>();

    public KeyboardManager() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        KeyStroke key = Globals.terminal.readInput();
                        if (key.getKeyType() != null) {
                            for (KeyboardListener kl : listeners) {
                                kl.receivedKey(key);
                            }
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void addListener(KeyboardListener listener) {
        listeners.add(listener);
    }
}
