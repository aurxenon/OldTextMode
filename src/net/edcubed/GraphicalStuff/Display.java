package net.edcubed.GraphicalStuff;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import net.edcubed.InputStuff.KeyboardManager;
import net.edcubed.NetworkStuff.NetworkManager;
import net.edcubed.TextMode.*;

import java.io.IOException;

public class Display {
    private Screen screen;
    private TerminalSize terminalSize;

    int columns;
    int rows;

    public Display() throws IOException{
        Globals.terminal = new DefaultTerminalFactory().createTerminal();
        System.out.println("created new terminal");
        Globals.terminal.enterPrivateMode();
        Globals.terminal.setBackgroundColor(TextColor.ANSI.GREEN);
        screen = new TerminalScreen(Globals.terminal);
        screen.startScreen();
        screen.setCursorPosition(null);
        terminalSize = Globals.terminal.getTerminalSize();
        Globals.terminal.clearScreen();
        columns = terminalSize.getColumns();
        rows = terminalSize.getRows();
    }

    public void drawSymbol(Symbol character, int x, int y) throws IOException{
        int r = character.getRed();
        int g = character.getGreen();
        int b = character.getBlue();
        TextColor textColor = new TextColor.RGB(r,g,b);
        TextColor backgroundColor = TextColor.ANSI.BLUE;
        TextCharacter drawCharacter = new TextCharacter(character.getSymbol(), textColor, backgroundColor);

        screen.setCharacter(x, y, drawCharacter);
        screen.refresh();
    }

    public void clear(){
        screen.clear();
    }
}
