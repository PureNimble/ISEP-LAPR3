package isep.lapr3.g094.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import isep.lapr3.g094.application.controller.GestorAgriculaController;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;

public class GestorAgriculaUI implements Runnable {

    private GestorAgriculaController controller = new GestorAgriculaController();

    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Registar Operação", () -> {
            try {
                registerOperation();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n====================Gestor Agricula UI====================");

            if (option != -1)
                options.get(option).run();
        } while (option != -1);

    }

    private void registerOperation() throws SQLException {
        List<String> options = new ArrayList<>();
        char[] operations = { 'S', 'M', 'C', 'A', 'P' };
        options.add("Semeadura");
        options.add("monda");
        options.add("Colheita");
        options.add("Aplicação de fator de produção");
        options.add("Po");

        Integer option = Utils.showAndSelectIndex(options, "\n====================Operações====================");

        if (option == -1)
            this.run();

        char operationType = operations[option];

        // controller.registerOperation(operdaationType, value, parcelaID, plantacaoID,
        // data);

    }

}
