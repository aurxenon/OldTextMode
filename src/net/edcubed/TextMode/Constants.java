package net.edcubed.TextMode;

import net.edcubed.GraphicalStuff.Symbol;

public class Constants {
    //eventually i want to load all of this from a config file
    public static final String VERSION = "alphav2.0";
    public static final boolean DEBUG = true;
    public static final Symbol PLAYER_CHAR = new Symbol('@', 255, 215, 0);
    public static final Symbol TERRAIN_CHAR = new Symbol('#', 0, 128, 0);
}
