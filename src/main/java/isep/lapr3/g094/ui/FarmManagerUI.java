package isep.lapr3.g094.ui;

import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Valor: ");
        int value = scanner.nextInt();

        System.out.println("Parcela ID: ");
        int parcelaID = scanner.nextInt();

        System.out.println("Plantação ID: ");
        int plantacaoID = scanner.nextInt();

        System.out.println("Data da operação: ");
        Date dataOperacao = Date.valueOf(scanner.nextLine());

        scanner.close();
        try {
            farmManagerController.registerOperation(operationType, value, parcelaID, plantacaoID, dataOperacao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
