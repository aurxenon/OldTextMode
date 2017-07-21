package net.edcubed.Smitty;

import java.util.ArrayList;
import net.edcubed.SmittyCommons.Place;

public class WorldManager {
    private ArrayList<Place> generatedTerrain;
    private ArrayList<Place> artificialTerrain;

    public WorldManager() {}

    public void setGeneratedTerrain(ArrayList<Place> cpuTerrain) {generatedTerrain = cpuTerrain;}
    public void setArtificialTerrain(ArrayList<Place> playerTerrain) {artificialTerrain = playerTerrain;}

    public ArrayList<Place> getGeneratedTerrain() {return generatedTerrain;}
    public ArrayList<Place> getArtificialTerrain() {return artificialTerrain;}
}
