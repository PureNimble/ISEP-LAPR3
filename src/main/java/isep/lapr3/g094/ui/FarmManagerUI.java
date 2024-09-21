package isep.lapr3.g094.ui;

import isep.lapr3.g094.application.controller.FarmManagerController;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FarmManagerUI implements Runnable {

    private FarmManagerController farmManagerController = new FarmManagerController();

    public void run() {

        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Registar Operação", new RegisterOperationUI()));
        options.add(new MenuItem("Ver Registos", new DisplayOperationUI()));
        options.add(new MenuItem("Anular Operação", this::cancelOperation));
        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options,
                    "\n====================Menu====================");
            if (option != -1) {
                options.get(option).run();
            }
        } while (option != -1);
    }

    private void cancelOperation() {
        Integer operacaoId = selectOperacao();
        if (operacaoId == null){
            System.out.println("\nOperação não encontrada");
            return;
        }

        try {
            farmManagerController.cancelOperation(operacaoId);
            System.out.println("\nOperação anulada com sucesso!");
        } catch (SQLException e) {
            System.out.println("\nErro ao anular operação");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private Integer selectOperacao(){
        Integer operacaoId = null;
        try {
            Map<String, Integer> operacoes = farmManagerController.getOperacoes();

            List<String> operacoesList = new ArrayList<String>(operacoes.keySet());

            String option = (String) Utils.showAndSelectOne(operacoesList, "\nLista de operacões:");
            if (option != null)
                operacaoId = operacoes.get(option);
        } catch (SQLException e) {
            System.out.println("\nErro ao obter lista de operações");
            System.out.println("Motivo: " + e.getMessage());
        }

        return operacaoId;
    }
}
