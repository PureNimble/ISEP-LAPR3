package isep.lapr3.g094.application.controller;

import java.sql.Date;
import java.sql.SQLException;

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
            case 'C':
                farmManagerRepository.registerColheita(quantidade, parcelaID, plantacaoID, dataOperacao);
            case 'P':
                farmManagerRepository.registerPoda(quantidade, parcelaID, plantacaoID, dataOperacao);
        }
    }

    public void registerSemeadura(double quantidade, int parcelaID, int plantacaoID, Date dataOperacao,
            double area)
            throws SQLException {
        farmManagerRepository.registerSemeadura(quantidade, parcelaID, plantacaoID, dataOperacao, area);
    }

    public void registerFatorDeProducao(double quantidade, int parcelaID, Date dataOperacao,
            int fatorProducaoID, int modoFertilizacaoID)
            throws SQLException {
        farmManagerRepository.registerFatorDeProducao(quantidade, parcelaID, dataOperacao,
                fatorProducaoID, modoFertilizacaoID);
    }

}