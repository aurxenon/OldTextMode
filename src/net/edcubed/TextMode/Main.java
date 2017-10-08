package net.edcubed.TextMode;

import java.io.IOException;
import java.util.ArrayList;

import net.edcubed.GraphicalStuff.Display;
import net.edcubed.InputStuff.KeyboardListener;
import net.edcubed.NetworkStuff.NetworkListener;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import net.edcubed.TextModeCommons.*;

public class Main implements KeyboardListener,NetworkListener{
    public static int x = 0;
    public static int y = 0;
    public static int[] coords = {x,y};
    public static Player me;
    public static ExtraUtils extraUtils = new ExtraUtils();
    public static WorldManager worldUtils = new WorldManager();
    public static int worldSizeX;
    public static int worldSizeY;

    public static void main(String[] args) {
        String username = Integer.toString(extraUtils.randInt(0,100));
        me = new Player(username, coords);
        Globals.networkManager.addListener(new Main());

        try{
            Globals.display = new Display();
        }catch(IOException e){
            e.printStackTrace();
        }

        //update thread
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(50);
                        Globals.networkManager.sendTCPData(new PlayerMP(PlayerMP.Status.PLAYER, me, Constants.VERSION));
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //keyboard input
        new Thread(new Runnable() {
            public void run() {
                while (true) {

                }
            }
        }).start();
    }

    //keyboard input
    public void receivedKey(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowRight) {
            me.moveRight();
        }
        if (key.getKeyType() == KeyType.ArrowLeft) {
            me.moveLeft();
        }
        if (key.getKeyType() == KeyType.ArrowUp) {
            me.moveUp();
        }
        if (key.getKeyType() == KeyType.ArrowDown) {
            me.moveDown();
        }
        if (key.getKeyType() == KeyType.Escape) {
            //networker.sendData(new PlayerMP(PlayerMP.Status.DISCONNECT, me, versionCode));
            extraUtils.exitGame();
        }
        x = me.getCoords()[0];
        y = me.getCoords()[1];
    }

    //dealing with messages
    public void receivedMessage(Object message) {
        if(message instanceof ArrayList<?>)
        {
            //tcp
            if(((ArrayList<?>)message).get(0) instanceof Place)
            {
                if (((ArrayList<Place>)message).get(0).getKind() == Place.TerrainKind.NATURAL) {
                    Main.worldUtils.setGeneratedTerrain((ArrayList<Place>)message);
                }
                if (((ArrayList<Place>)message).get(0).getKind() == Place.TerrainKind.ARTIFICIAL) {
                    Main.worldUtils.setArtificialTerrain((ArrayList<Place>)message);
                }
            }
            if(((ArrayList<?>)message).get(0) instanceof Integer)
            {
                ArrayList<Integer> worldSize = (ArrayList<Integer>)message;
                Main.worldSizeX = worldSize.get(0);
                Main.worldSizeY = worldSize.get(1);
            }

            //udp
            if(((ArrayList<?>)message).get(0) instanceof Player)
            {
                Main.extraUtils.setGamePlayers((ArrayList<Player>)message);
                Main.DrawScreen();
            }
            if(((ArrayList<?>)message).get(0) instanceof Place)
            {
                if (((ArrayList<Place>)message).get(0).getKind() == Place.TerrainKind.NATURAL) {
                    Main.worldUtils.setGeneratedTerrain((ArrayList<Place>)message);
                    Main.DrawScreen();
                }
                if (((ArrayList<Place>)message).get(0).getKind() == Place.TerrainKind.ARTIFICIAL) {
                    Main.worldUtils.setArtificialTerrain((ArrayList<Place>)message);
                    Main.DrawScreen();
                }
            }
            if(((ArrayList<?>)message).get(0) instanceof Integer)
            {
                ArrayList<Integer> worldSize = (ArrayList<Integer>)message;
                Main.worldSizeX = worldSize.get(0);
                Main.worldSizeY = worldSize.get(1);
            }
        }
        if (message instanceof String) {
            //tcp
            System.out.println((String) message);
            if (message == "i'll always be here for you :)") {
                System.out.println(":)");
            }
        }
    }

    //just draws the screen every second
    public static void DrawScreen() {
        try{
            //always draw terrain first
            Globals.display.clear();
            if (worldUtils.getGeneratedTerrain() != null) {
                for (int i = 0; i < worldUtils.getGeneratedTerrain().size(); i++) {
                    int terrainX = worldUtils.getGeneratedTerrain().get(i).getX();
                    int terrainY = worldUtils.getGeneratedTerrain().get(i).getY();
                    Globals.display.drawSymbol(Constants.TERRAIN_CHAR, terrainX, terrainY);
                }
            }

            //always draw players last
            for (int i = 0;  i < extraUtils.getGamePlayers().size(); i++) {
                int playerX = extraUtils.getGamePlayers().get(i).getCoords()[0];
                int playerY = extraUtils.getGamePlayers().get(i).getCoords()[1];
                Globals.display.drawSymbol(Constants.PLAYER_CHAR, playerX, playerY);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}