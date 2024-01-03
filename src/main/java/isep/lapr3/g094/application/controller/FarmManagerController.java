package isep.lapr3.g094.application.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import isep.lapr3.g094.repository.FarmManagerRepository;
import isep.lapr3.g094.repository.Repositories;

public class FarmManagerController {

    private FarmManagerRepository farmManagerRepository;

    public FarmManagerController() {
        getFarmManagerRepository();
    }

    private FarmManagerRepository getFarmManagerRepository() {
        if (farmManagerRepository == null) {
            Repositories repositories = Repositories.getInstance();

            farmManagerRepository = repositories.getFarmManagerRepository();
        }
        return farmManagerRepository;
    }

    // Your code here
    public void registerOperation(char operationType, double quantidade, int parcelaID, int plantacaoID,
            Date dataOperacao)
            throws SQLException {
        switch (Character.toUpperCase(operationType)) {
            case 'M':
                farmManagerRepository.registerMonda(quantidade, parcelaID, plantacaoID, dataOperacao);
                break;
            case 'C':
                farmManagerRepository.registerColheita(quantidade, parcelaID, plantacaoID, dataOperacao);
                break;
            case 'P':
                farmManagerRepository.registerPoda(quantidade, parcelaID, plantacaoID, dataOperacao);
                break;
        }
    }

    public void registerSemeadura(double quantidade, int parcelaID, int plantacaoID, Date dataOperacao,
            double area)
            throws SQLException {
        farmManagerRepository.registerSemeadura(quantidade, parcelaID, plantacaoID, dataOperacao, area);
    }

    public void registerFatorDeProducao(double quantidade, int parcelaID, Date dataOperacao, double area,
            int fatorProducaoID, int modoFertilizacaoID)
            throws SQLException {
        farmManagerRepository.registerFatorDeProducao(quantidade, parcelaID, dataOperacao, area,
                fatorProducaoID, modoFertilizacaoID);
    }

    public void registerRega(int duracao, int setorID, Date dataOperacao, Time hora, int estado)
            throws SQLException {
        farmManagerRepository.registerRega(duracao, setorID, dataOperacao, hora, estado);
    }

    public void registerFertirrega(int duracao, int setorID, Date dataOperacao, Time hora, int estado, int mixID)
            throws SQLException {
        farmManagerRepository.registerFertirrega(duracao, setorID, dataOperacao, hora, estado, mixID);
    }

    public Map<String, Integer> getParcelas() throws SQLException {
        return farmManagerRepository.getParcelas();
    }

    public Map<String, Integer> getPlantacoes(int parcelaID, Date operacaoData) throws SQLException {
        return farmManagerRepository.getPlantacoes(parcelaID, operacaoData);
    }

    public Map<String, Integer> getCulturas() throws SQLException {
        return farmManagerRepository.getCulturas();
    }

    public Map<String, Integer> getFatorProducao() throws SQLException {
        return farmManagerRepository.getFatorProducao();
    }

    public Map<String, Integer> getModoFertilizacao() throws SQLException {
        return farmManagerRepository.getModoFertilizacao();
    }

    public Map<String, Integer> getProdutos(int parcelaID) throws SQLException {
        return farmManagerRepository.getProdutos(parcelaID);
    }

    public Map<String, Integer> getPlantacaoByProduto(int parcelaID, int produtoID) throws SQLException {
        return farmManagerRepository.getPlantacaoByProduto(parcelaID, produtoID);
    }

    public Map<String, Integer> getSetor() throws SQLException {
        return farmManagerRepository.getSetor();
    }

    public Map<String, Integer> getMix() throws SQLException {
        return farmManagerRepository.getMix();
    }

    public List<String> getOperation(char operationType, int parcelaID, Date dataInicial, Date dataFinal)
            throws SQLException {
        switch (Character.toUpperCase(operationType)) {
            case 'C':
                return farmManagerRepository.getProdutosColhidosList(parcelaID, dataInicial, dataFinal);
            case 'E':
                return farmManagerRepository.getFatorProducaoElementosList(parcelaID, dataInicial, dataFinal);
            case 'O':
                return farmManagerRepository.GetOperacaoList(parcelaID, dataInicial, dataFinal);
        }
        return new ArrayList<String>();
    }

    public List<String> getOperationByDate(char operationType, Date dataInicial, Date dataFinal)
            throws SQLException {
        switch (Character.toUpperCase(operationType)) {
            case 'F':
                return farmManagerRepository.getFatoresProducaoList(dataInicial, dataFinal);
            case 'R':
                return farmManagerRepository.getRegaMensal(dataInicial, dataFinal);
        }
        return new ArrayList<String>();
    }

    public int createReceita(String designacao)
            throws SQLException {
        return farmManagerRepository.createReceita(designacao);
    }

    public void addFatorToReceita(int receitaID, int fatorProducaoID, double quantidade, String unidade)
            throws SQLException {
        farmManagerRepository.addFatorToReceita(receitaID, fatorProducaoID, quantidade, unidade);
    }

    public Map<Integer, List<String>> getConsumoByCultura(int year) throws SQLException {
        return farmManagerRepository.getConsumoByCultura(year);
    }

    public Map<String, List<Integer>> getFatorProducaoYear(int year) throws SQLException {
        return farmManagerRepository.getFatorProducaoYear(year);
    }

    public void cancelOperation(int operacaoId) throws SQLException {
        farmManagerRepository.cancelOperation(operacaoId);
    }

    public Map<String, Integer> getOperacoes() throws SQLException {
        return farmManagerRepository.getOperacoes();
    }
}