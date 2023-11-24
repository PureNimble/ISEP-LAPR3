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
    public boolean registerOperation(char operationType, double quantidade, int parcelaID, int plantacaoID, Date dataOperacao)
            throws SQLException {
        switch (Character.toUpperCase(operationType)) {
            case 'M':
                return farmManagerRepository.registerMonda(quantidade, parcelaID, plantacaoID, dataOperacao);
            case 'R':
                return farmManagerRepository.registerColheita(quantidade, parcelaID, plantacaoID, dataOperacao);
            case 'F':
                return farmManagerRepository.registerFatorDeProducao(quantidade, parcelaID, plantacaoID, dataOperacao);
            case 'P':
                return farmManagerRepository.registerPoda(quantidade, parcelaID, plantacaoID, dataOperacao);
            default:
                return false;
        }
    }

    public boolean registerSemeadura(char operationType, double quantidade, int parcelaID, int plantacaoID, Date dataOperacao,
            double area)
            throws SQLException {
        return farmManagerRepository.registerSemeadura(quantidade, parcelaID, plantacaoID, dataOperacao, area);
    }

}