package isep.lapr3.g094.ui.menu;

import isep.lapr3.g094.ui.ImportXlsxFileUI;
import isep.lapr3.g094.ui.DevTeamUI;
import isep.lapr3.g094.ui.IrrigationUI;
import isep.lapr3.g094.ui.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class MainMenuUI implements Runnable {

    public void run() {

        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Aceder ao controlador do Sistema de Rega", new IrrigationUI()));
        options.add(new MenuItem("Adicionar informação do sistema legacy", new ImportXlsxFileUI()));
        options.add(new MenuItem("Equipa de Desenvolvimento", new DevTeamUI()));
        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options,
                    "\n====================Menu====================");
            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }

}
