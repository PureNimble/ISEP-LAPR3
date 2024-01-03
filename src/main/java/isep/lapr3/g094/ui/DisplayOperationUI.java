package isep.lapr3.g094.ui;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import isep.lapr3.g094.application.controller.FarmManagerController;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;

public class DisplayOperationUI implements Runnable {

    private FarmManagerController farmManagerController = new FarmManagerController();

    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("USBD16 - Lista de produtos colhidos", () -> checkOperation('C')));
        options.add(new MenuItem("USBD17 - Lista de quantidades por elemento para fatores de produção aplicados",
                () -> checkOperation('E')));
        options.add(new MenuItem("USBD18 - Lista de operações realizadas", () -> checkOperation('O')));
        options.add(new MenuItem("USBD19 - Lista de aplicações de fator de produção", () -> checkOperation('F')));
        options.add(new MenuItem("USBD20 - Lista de rega mensal", () -> checkOperation('R')));
        options.add(new MenuItem("USBD33 - Lista de culturas com maior consumo de água", () -> checkOperation('A')));
        options.add(
                new MenuItem("USBD34 - Lista das substâncias de fatores de produção não usadas no ano civil indicado.",
                        () -> checkOperation('S')));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options,
                    "\n=========Interface do Gestor Agrícola=========");
            if (option != -1)
                options.get(option).run();

        } while (option != -1);
    }

    private void checkOperation(char operationType) {
        if (operationType == 'A') {
            getConsumoByCultura();
            return;
        }

        if (operationType == 'S') {
            getFatorProducaoYear();
            return;
        }

        java.util.Date utilDateInicial = Utils.readDateFromConsole("Data de Inicio: ");
        java.sql.Date dataInicial = java.sql.Date
                .valueOf(utilDateInicial.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        java.util.Date utilDateFinal = Utils.readDateFromConsole("Data de Fim: ");
        java.sql.Date dataFinal = java.sql.Date
                .valueOf(utilDateFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        List<String> list = new ArrayList<String>();
        try {

            if (operationType != 'F' && operationType != 'R') {
                Integer parcelaID = selectParcela();
                if (parcelaID == null)
                    return;
                list = farmManagerController.getOperation(operationType, parcelaID, dataInicial, dataFinal);

            } else
                list = farmManagerController.getOperationByDate(operationType, dataInicial, dataFinal);
        } catch (SQLException e) {
            System.out.println("\nErro na obtenção da lista de operações");
            System.out.println("Motivo: " + e.getMessage());
        }
        if (!list.isEmpty()) {
            System.out.println("\nResultado:");
            for (String s : list)
                System.out.println(s);
        } else
            System.out.println("\nSem resultados");
    }

    private Integer selectParcela() {
        Integer parcelaID = null;
        try {
            Map<String, Integer> parcelas = farmManagerController.getParcelas();

            List<String> parcelasList = new ArrayList<String>(parcelas.keySet());

            String option = (String) Utils.showAndSelectOne(parcelasList, "\nLista de parcelas:");
            if (option != null)
                parcelaID = parcelas.get(option);
        } catch (SQLException e) {
            System.out.println("\nErro ao obter lista de parcelas");
            System.out.println("Motivo: " + e.getMessage());
        }

        return parcelaID;

    }

    private void getConsumoByCultura() {
        Integer year = Utils.readIntegerFromConsole("Ano civil: ");

        try {
            Map<Integer, List<String>> list = farmManagerController.getConsumoByCultura(year);
            if (!list.isEmpty()) {
                System.out.println("\nResultado:");
                int i = 1;
                for (Map.Entry<Integer, List<String>> entry : list.entrySet()) {
                    System.out.println("Top " + i + "º de consumo: " + entry.getKey());
                    for (String s : entry.getValue()) {
                        System.out.println("\t -> " + s);
                    }
                    i++;
                }
            } else
                System.out.println("\nSem resultados");
        } catch (SQLException e) {
            System.out.println("\nErro na obtenção da lista de operações");
            System.out.println("Motivo: " + e.getMessage());
        }

    }

    private void getFatorProducaoYear() {
        Integer year = Utils.readIntegerFromConsole("Ano civil: ");

        try {
            Map<String, List<Integer>> map = farmManagerController.getFatorProducaoYear(year);

            if (!map.isEmpty()) {
                System.out.println("\nResultado:");
                System.out.println("Fatores de Producao:");
                for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
                    System.out.print("\t -> " + entry.getKey());
                    for (Integer currentYear : entry.getValue()) {
                        System.out.print(", " + currentYear);
                    }
                    System.out.println();
                }
            } else
                System.out.println("\nSem resultados");
        } catch (SQLException e) {
            System.out.println("\nErro na obtenção da lista de operações");
            System.out.println("Motivo: " + e.getMessage());
        }

    }
}