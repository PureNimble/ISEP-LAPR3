package isep.lapr3.g094.application.controller;

import java.sql.Date;
import java.sql.SQLException;
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

    public Map<String, Integer> getParcelas() throws SQLException {
        return farmManagerRepository.getParcelas();
    }

    public Map<String, Integer> getPlantacoes(int parcelaID) throws SQLException {
        return farmManagerRepository.getPlantacoes(parcelaID);
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
}