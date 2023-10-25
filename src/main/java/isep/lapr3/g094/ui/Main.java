package isep.lapr3.g094.ui;

import java.io.IOException;

import isep.lapr3.g094.Controller.CreateSQLInserts;

public class Main {
    static ControllerUI controllerUI = new ControllerUI();
    static CreateSQLInserts createSQLInserts = new CreateSQLInserts();
    static MenuUI menuUI = new MenuUI();

    public static void main(String[] args) throws NumberFormatException, IOException {
        menuUI.main();
    }
}
