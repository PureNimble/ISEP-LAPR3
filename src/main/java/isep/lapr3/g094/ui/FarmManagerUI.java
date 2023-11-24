package isep.lapr3.g094.ui;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import isep.lapr3.g094.application.controller.FarmManagerController;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;

public class FarmManagerUI implements Runnable {

    private FarmManagerController farmManagerController = new FarmManagerController();

    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Semeadura", () -> createOperation('S')));
        options.add(new MenuItem("Monda", () -> createOperation('M')));
        options.add(new MenuItem("Colheita", () -> createOperation('C')));
        options.add(new MenuItem("Aplicação de fator de produção", () -> createOperation('A')));
        options.add(new MenuItem("Poda", () -> createOperation('P')));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options,
                    "\n=========Interface do Gestor Agrícola=========");
            if (option != -1)
                options.get(option).run();

        } while (option != -1);
    }

    private void createOperation(char operationType) {

        int parcelaID = Utils.readIntegerFromConsole("Parcela ID: ");
        double quantidade = Utils.readDoubleFromConsole("Quantidade: ");
        java.util.Date utilDate = Utils.readDateFromConsole("Data da operação: ");
        java.sql.Date dataOperacao = java.sql.Date
                .valueOf(utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        try {
            if (operationType == 'S') {
                int culturaID = Utils.readIntegerFromConsole("Cultura ID: ");
                double area = Utils.readDoubleFromConsole("Area: ");
                farmManagerController.registerSemeadura(operationType, quantidade, parcelaID, culturaID, dataOperacao,
                        area);
            } else{
                int plantacaoID = Utils.readIntegerFromConsole("Plantação ID: ");
                farmManagerController.registerOperation(operationType, quantidade, parcelaID, plantacaoID, dataOperacao);
            }
        } catch (SQLException e) {
            System.out.println("\nErro ao registar operação");
            System.out.println("Motivo: " + e.getMessage());
        }
    }
}
