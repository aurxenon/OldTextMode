package net.edcubed.Smitty;

import java.util.ArrayList;
import java.util.Random;
import net.edcubed.SmittyCommons.*;

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
}