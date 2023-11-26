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
        options.add(new MenuItem("Lista de produtos colhidos", () -> checkOperation('C')));
        options.add(new MenuItem("Lista de quantidades por elemento para fatores de produção aplicados",
                () -> checkOperation('E')));
        options.add(new MenuItem("Lista de operações realizadas", () -> checkOperation('O')));
        options.add(new MenuItem("Lista de aplicações de fator de produção", () -> checkOperation('F')));
        options.add(new MenuItem("Lista de rega mensal", () -> checkOperation('R')));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options,
                    "\n=========Interface do Gestor Agrícola=========");
            if (option != -1)
                options.get(option).run();

        } while (option != -1);
    }

    private void checkOperation(char operationType) {
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
}
