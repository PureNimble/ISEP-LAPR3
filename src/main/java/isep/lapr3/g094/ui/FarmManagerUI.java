package isep.lapr3.g094.ui;

import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class FarmManagerUI implements Runnable {

    public void run() {

        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Registar Operação", new RegisterOperationUI()));
        options.add(new MenuItem("Ver Registos", new DisplayOperationUI()));
        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options,
                    "\n====================Menu====================");
            if (option != -1) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
