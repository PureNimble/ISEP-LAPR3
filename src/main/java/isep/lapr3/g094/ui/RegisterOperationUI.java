package isep.lapr3.g094.ui;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import isep.lapr3.g094.application.controller.FarmManagerController;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;

public class RegisterOperationUI implements Runnable {

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
        Integer parcelaID = selectParcela();
        if (parcelaID == null)
            return;

        double quantidade = Utils.readDoubleFromConsole("Quantidade: ");
        java.util.Date utilDate = Utils.readDateFromConsole("Data da operação: ");
        java.sql.Date dataOperacao = java.sql.Date
                .valueOf(utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        try {
            if (operationType == 'S') {
                Integer culturaID = selectCultura();
                if (culturaID == null)
                    return;
                double area = Utils.readDoubleFromConsole("Area: ");
                farmManagerController.registerSemeadura(quantidade, parcelaID, culturaID, dataOperacao,
                        area);
            } else if (operationType == 'A') {
                double area = Utils.readDoubleFromConsole("Area: ");
                Integer fatorProducaoID = selectFatorProducao();
                if (fatorProducaoID == null)
                    return;
                Integer modoFertilizacaoID = selectModoFertilizacao();
                if (modoFertilizacaoID == null)
                    return;
                farmManagerController.registerFatorDeProducao(quantidade, parcelaID, dataOperacao, area,
                        fatorProducaoID, modoFertilizacaoID);
            } else {
                Integer plantacaoID = null;
                if (operationType == 'C') {
                    Integer produtoID = selectProduto(parcelaID);
                    if (produtoID == null)
                        return;

                    plantacaoID = selectPlantacaoByProduto(parcelaID, produtoID);
                    if (plantacaoID == null)
                        return;
                } else {
                    plantacaoID = selectPlantacao(parcelaID);
                    if (plantacaoID == null)
                        return;

                }
                farmManagerController.registerOperation(operationType, quantidade, parcelaID, plantacaoID,
                        dataOperacao);
            }
            System.out.println("\nOperação registada com sucesso!");
        } catch (SQLException e) {
            System.out.println("\nErro ao registar operação");
            System.out.println("Motivo: " + e.getMessage());
        }
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

    private Integer selectPlantacao(int parcelaID) {
        Integer plantacaoID = null;
        try {
            Map<String, Integer> plantacoes = farmManagerController.getPlantacoes(parcelaID);

            List<String> plantacoesList = new ArrayList<String>(plantacoes.keySet());
            String plantacao = (String) Utils.showAndSelectOne(plantacoesList, "\nLista de plantações:");

            plantacaoID = plantacoes.get(plantacao);

        } catch (SQLException e) {
            System.out.println("\nErro ao obter lista de plantações");
            System.out.println("Motivo: " + e.getMessage());
        }

        return plantacaoID;

    }

    private Integer selectCultura() {
        Integer culturaID = null;
        try {
            Map<String, Integer> culturas = farmManagerController.getCulturas();

            List<String> culturasList = new ArrayList<String>(culturas.keySet());
            String cultura = (String) Utils.showAndSelectOne(culturasList, "\nLista de culturas:");

            culturaID = culturas.get(cultura);

        } catch (SQLException e) {
            System.out.println("\nErro ao obter lista de culturas");
            System.out.println("Motivo: " + e.getMessage());
        }

        return culturaID;
    }

    private Integer selectFatorProducao() {
        Integer fatorProducaoID = null;
        try {
            Map<String, Integer> fatoresProducao = farmManagerController.getFatorProducao();

            List<String> fatorProducaoList = new ArrayList<String>(fatoresProducao.keySet());
            String fatorProducao = (String) Utils.showAndSelectOne(fatorProducaoList,
                    "\nLista de fatores de produção:");

            fatorProducaoID = fatoresProducao.get(fatorProducao);

        } catch (SQLException e) {
            System.out.println("\nErro ao obter lista de de fatores de produção");
            System.out.println("Motivo: " + e.getMessage());
        }

        return fatorProducaoID;
    }

    private Integer selectModoFertilizacao() {
        Integer modoFertilizacaoID = null;
        try {
            Map<String, Integer> modosFertilizacao = farmManagerController.getModoFertilizacao();

            List<String> modoFertilizacaoList = new ArrayList<String>(modosFertilizacao.keySet());
            String fatorProducao = (String) Utils.showAndSelectOne(modoFertilizacaoList,
                    "\nLista de modos de fertilização:");

            modoFertilizacaoID = modosFertilizacao.get(fatorProducao);

        } catch (SQLException e) {
            System.out.println("\nErro ao obter lista de de modos de fertilização");
            System.out.println("Motivo: " + e.getMessage());
        }

        return modoFertilizacaoID;
    }

    private Integer selectProduto(int parcelaID) {
        Integer produtoID = null;
        try {
            Map<String, Integer> produtos = farmManagerController.getProdutos(parcelaID);

            List<String> produtosList = new ArrayList<String>(produtos.keySet());
            String produto = (String) Utils.showAndSelectOne(produtosList,
                    "\nLista de produtos:");

            produtoID = produtos.get(produto);

        } catch (SQLException e) {
            System.out.println("\nErro ao obter lista de de produtos");
            System.out.println("Motivo: " + e.getMessage());
        }

        return produtoID;
    }

    private Integer selectPlantacaoByProduto(int parcelaID, int produtoID) {
        Integer plantacaoID = null;
        try {
            Map<String, Integer> plantacoes = farmManagerController.getPlantacaoByProduto(parcelaID, produtoID);

            List<String> plantacoesList = new ArrayList<String>(plantacoes.keySet());
            String plantacao = (String) Utils.showAndSelectOne(plantacoesList, "\nLista de plantações:");

            plantacaoID = plantacoes.get(plantacao);

        } catch (SQLException e) {
            System.out.println("\nErro ao obter lista de plantações");
            System.out.println("Motivo: " + e.getMessage());
        }

        return plantacaoID;
    }
}
