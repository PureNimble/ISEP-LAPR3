package isep.lapr3.g094.application.controller;

import java.sql.Date;
import java.sql.SQLException;
import isep.lapr3.g094.repository.FarmManagerRepository;
import isep.lapr3.g094.repository.Repositories;

public class FarmManagerController {
    // Your code here
    public boolean registerOperation(char operationType, int value, int parcelaID, int plantacaoID, Date dataOperacao)
            throws SQLException {
        FarmManagerRepository repository = Repositories.getInstance().getGestorAgricolaRepository();
        switch (Character.toUpperCase(operationType)) {
        case 'M':
            return repository.registerMonda(value, parcelaID, plantacaoID, dataOperacao);
        case 'S':
            return repository.registerSemeadura(value, parcelaID, plantacaoID, dataOperacao);
        case 'R':
            return repository.registerColheita(value, parcelaID, plantacaoID, dataOperacao);
        case 'F':
            return repository.registerFatorDeProducao(value, parcelaID, plantacaoID, dataOperacao);
        case 'P':
            return repository.registerPoda(value, parcelaID, plantacaoID, dataOperacao);
        default:
            return false;
        }
    }
}