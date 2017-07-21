package net.edcubed.Smitty;

import java.io.IOException;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.screen.TerminalScreen;
import net.edcubed.SmittyCommons.*;

public class Main{
    public static int x = 0;
    public static int y = 0;
    public static int[] coords = {x,y};
    public static Player me;
    public static ExtraUtils extraUtils = new ExtraUtils();
    public static Terminal terminal;
    public static Screen screen;
    public static WorldManager worldUtils = new WorldManager();
    public static int worldSizeX;
    public static int worldSizeY;
    public static String versionCode = "alphav1.0";

    public static void main(String[] args) throws IOException{
        terminal = new DefaultTerminalFactory().createTerminal();
        terminal.enterPrivateMode();
        terminal.setBackgroundColor(TextColor.ANSI.GREEN);
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null);
        TerminalSize terminalSize = terminal.getTerminalSize();

        int column = terminalSize.getColumns();
        int row = terminalSize.getRows();
        String username = Integer.toString(extraUtils.randInt(0,100));
        me = new Player(username, coords);
        terminal.clearScreen();
        NetworkManager networker = new NetworkManager("localhost");
        networker.start();
        TCPNetworkManager tcpNetworker = new TCPNetworkManager("localhost", 26655);
        tcpNetworker.start();

        //update thread
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(50);
                        networker.sendData(new PlayerMP(PlayerMP.Status.PLAYER, me, versionCode));
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //tcp keep alive thread thread
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(15000);
                        tcpNetworker.sendData("i'm still here please don't leave me");
                        System.out.println("Sending keep alive");
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
                    try {
                        KeyStroke p = terminal.readInput();
                        if (p.getKeyType() != null){
                            if (p.getKeyType() == KeyType.ArrowRight) {
                                me.moveRight();
                            }
                            if (p.getKeyType() == KeyType.ArrowLeft) {
                                me.moveLeft();
                            }
                            if (p.getKeyType() == KeyType.ArrowUp) {
                                me.moveUp();
                            }
                            if (p.getKeyType() == KeyType.ArrowDown) {
                                me.moveDown();
                            }
                            if (p.getKeyType() == KeyType.Escape) {
                                networker.sendData(new PlayerMP(PlayerMP.Status.DISCONNECT, me, versionCode));
                                terminal.exitPrivateMode();
                                terminal.close();
                                System.exit(0);
                            }
                        }
                        x = me.getCoords()[0];
                        y = me.getCoords()[1];
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //just draws the screen every second
    public static void DrawScreen() {
        try{
            screen.clear();
            //always draw terrain first
            if (worldUtils.getGeneratedTerrain() != null) {
                TextCharacter terrainChar = new TextCharacter('#', TextColor.ANSI.GREEN, TextColor.ANSI.BLUE);
                for (int i = 0; i < worldUtils.getGeneratedTerrain().size(); i++) {
                    int terrainX = worldUtils.getGeneratedTerrain().get(i).getX();
                    int terrainY = worldUtils.getGeneratedTerrain().get(i).getY();
                    screen.setCharacter(terrainX,terrainY,terrainChar);
                }
            }

            //always draw players last
            for (int i = 0;  i < extraUtils.getGamePlayers().size(); i++) {
                TextCharacter playerChar = new TextCharacter('@', TextColor.ANSI.YELLOW, TextColor.ANSI.GREEN);
                int playerX = extraUtils.getGamePlayers().get(i).getCoords()[0];
                int playerY = extraUtils.getGamePlayers().get(i).getCoords()[1];
                screen.setCharacter(playerX,playerY,playerChar);
            }
            screen.refresh();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}