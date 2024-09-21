package isep.lapr3.g094.ui;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
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
        options.add(new MenuItem("USBD11 - Semeadura", () -> createOperation('S')));
        options.add(new MenuItem("USBD12 - Monda", () -> createOperation('M')));
        options.add(new MenuItem("USBD13 - Colheita", () -> createOperation('C')));
        options.add(new MenuItem("USBD14 - Aplicação de fator de produção", () -> createOperation('A')));
        options.add(new MenuItem("USBD15 - Poda", () -> createOperation('P')));
        options.add(new MenuItem("USBD31 - Receita", () -> createReceita()));
        options.add(new MenuItem("USBD32 - Rega", () -> createRega()));

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
        java.sql.Date dataOperacao = new java.sql.Date(utilDate.getTime());

        try {
            switch (operationType) {
                case 'S':
                    registerSemeadura(parcelaID, quantidade, dataOperacao);
                    break;
                case 'A':
                    registerFatorDeProducao(parcelaID, quantidade, dataOperacao);
                    break;
                default:
                    registerOperation(parcelaID, quantidade, dataOperacao, operationType);
                    break;
            }
            System.out.println("\nOperação registada com sucesso!");
        } catch (SQLException e) {
            System.out.println("\nErro ao registar operação");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private void registerSemeadura(Integer parcelaID, double quantidade, java.sql.Date dataOperacao)
            throws SQLException {
        Integer culturaID = selectCultura();
        if (culturaID == null)
            return;
        double area = Utils.readDoubleFromConsole("Area: ");
        farmManagerController.registerSemeadura(quantidade, parcelaID, culturaID, dataOperacao, area);
    }

    private void registerFatorDeProducao(Integer parcelaID, double quantidade, java.sql.Date dataOperacao)
            throws SQLException {
        double area = Utils.readDoubleFromConsole("Area: ");
        Integer fatorProducaoID = selectFatorProducao();
        if (fatorProducaoID == null)
            return;
        Integer modoFertilizacaoID = selectModoFertilizacao();
        if (modoFertilizacaoID == null)
            return;
        farmManagerController.registerFatorDeProducao(quantidade, parcelaID, dataOperacao, area, fatorProducaoID,
                modoFertilizacaoID);
    }

    private void createRega() {

        List<String> options = new ArrayList<>();
        options.add("Registar uma rega no futuro");
        options.add("Registar uma rega executada");
        int estado = Utils.showAndSelectIndex(options,
                "\nDeseja registar uma rega no futuro ou uma rega executada?") + 1;
        if (estado == 0)
            return;
        Integer setorID = selectSetor();
        if (setorID == null)
            return;
        int duracao = Utils.readIntegerFromConsole("Duração: ");
        java.util.Date utilDate = Utils.readDateFromConsole("Data da operação: ");
        java.sql.Date dataOperacao = new java.sql.Date(utilDate.getTime());

        LocalTime localTime = Utils.readTimeFromConsole("Hora da operação: ");
        Time time = Time.valueOf(localTime);

        boolean confirm = Utils.confirm("Deseja adicionar uma receita à mistura?");
        Integer mixID = null;
        if (confirm) {
            mixID = selectMix();
            if (mixID == null)
                return;
        }
        try {
            if (confirm)
                farmManagerController.registerFertirrega(duracao, setorID, dataOperacao, time, estado, mixID);
            else
                farmManagerController.registerRega(duracao, setorID, dataOperacao, time, estado);
            System.out.println("\nOperação registada com sucesso!");
        } catch (SQLException e) {
            System.out.println("\nErro ao registar operação");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    public void createReceita() {
        String designacao = Utils.readLineFromConsole("Designação desejada para a receita: ");
        try {
            int receitaID = farmManagerController.createReceita(designacao);
            System.out.println("\nReceita inicializada com sucesso!");
            boolean cont;
            do {
                Integer fatorProducaoID = selectFatorProducao();
                Double quantidade = Utils.readDoubleFromConsole("Quantidade: ");
                String unidade = Utils.readLineFromConsole("Unidade: ");
                try {
                    farmManagerController.addFatorToReceita(receitaID, fatorProducaoID, quantidade, unidade);
                    System.out.println("\nFator adicionado com sucesso!");
                } catch (SQLException e) {
                    System.out.println("\nErro ao adicionar fator");
                    System.out.println("Motivo: " + e.getMessage());
                }
                cont = Utils.confirm("Deseja adicionar mais um fator?");
            } while (cont);
        } catch (SQLException e) {
            System.out.println("\nErro ao inicializar receita");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private void registerOperation(Integer parcelaID, double quantidade, java.sql.Date dataOperacao, char operationType)
            throws SQLException {
        Integer plantacaoID = null;
        if (operationType == 'C') {
            Integer produtoID = selectProduto(parcelaID);
            if (produtoID == null)
                return;

            plantacaoID = selectPlantacaoByProduto(parcelaID, produtoID);
            if (plantacaoID == null)
                return;
        } else {
            plantacaoID = selectPlantacao(parcelaID, dataOperacao);
            if (plantacaoID == null)
                return;
        }
        farmManagerController.registerOperation(operationType, quantidade, parcelaID, plantacaoID, dataOperacao);
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

    private Integer selectPlantacao(int parcelaID, Date dataOperacao) {
        Integer plantacaoID = null;
        try {
            Map<String, Integer> plantacoes = farmManagerController.getPlantacoes(parcelaID, dataOperacao);

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

    private Integer selectSetor() {
        Integer setorID = null;
        try {
            Map<String, Integer> setores = farmManagerController.getSetor();

            List<String> setorList = new ArrayList<String>();
            for (String setor : setores.keySet()) {
                setorList.add(setores.get(setor) + " - " + setor);
            }

            String option = (String) Utils.showAndSelectOne(setorList, "\nLista de Setores:");
            if (option != null) {
                String[] split = option.split(" - ");
                setorID = Integer.parseInt(split[0]);
            }
        } catch (SQLException e) {
            System.out.println("\nErro ao obter lista de setores");
            System.out.println("Motivo: " + e.getMessage());
        }

        return setorID;
    }

    private Integer selectMix() {
        Integer mixID = null;
        try {
            Map<String, Integer> receitas = farmManagerController.getMix();

            List<String> receitaList = new ArrayList<String>(receitas.keySet());

            String option = (String) Utils.showAndSelectOne(receitaList, "\nLista de Receitas:");
            if (option != null)
                mixID = receitas.get(option);
        } catch (SQLException e) {
            System.out.println("\nErro ao obter lista de receitas");
            System.out.println("Motivo: " + e.getMessage());
        }

        return mixID;
    }
}
