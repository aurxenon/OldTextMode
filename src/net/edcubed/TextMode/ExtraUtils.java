package net.edcubed.TextMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import net.edcubed.NetworkStuff.NetworkManager;
import net.edcubed.TextModeCommons.*;
import net.edcubed.TextMode.*;

public class ExtraUtils {
    ArrayList<Player> gamePlayers = new ArrayList<Player>();

    public ExtraUtils(){}
    public ArrayList<Player> getGamePlayers() {return gamePlayers;}
    public void setGamePlayers(ArrayList<Player> players) {gamePlayers = players;}
    public void addGamePlayer(Player player) {gamePlayers.add(player);}
    public void deleteGamePlayer(Player player) {gamePlayers.remove(player);}
    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    public void exitGame() {
        try {
            Globals.networkManager.disconnect();
            Globals.terminal.exitPrivateMode();
            Globals.terminal.close();
            System.exit(0);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void log(Object data) {
        System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + " - " + data.toString());
    }
}