package net.edcubed.GraphicalStuff;

public class Symbol {
    private char character;
    private int red = 255;
    private int green = 255;
    private int blue = 255;
    public Symbol(char character, int red, int green, int blue) {
        this.character = character;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public char getSymbol() {
        return character;
    }
    public int getRed() {
        return red;
    }
    public int getGreen() {
        return green;
    }
    public int getBlue() {
        return blue;
    }
    public void setCharacter(char character) {
        this.character = character;
    }
    public void setRed(int red){
        this.red = red;
    }
    public void setGreen(int green) {
        this.green = green;
    }
    public void setBlue(int blue) {
        this.blue = blue;
    }
}