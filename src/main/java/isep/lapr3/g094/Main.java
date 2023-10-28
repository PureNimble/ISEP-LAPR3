package isep.lapr3.g094;
import java.io.IOException;
import java.text.ParseException;

import isep.lapr3.g094.ui.MenuUI;

public class Main {
    
    static MenuUI menuUI = new MenuUI();
    public static void main(String[] args) throws IOException, NumberFormatException, ParseException {
        menuUI.mainUI();
    }
}
