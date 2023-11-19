package isep.lapr3.g094.application.controller;

import java.sql.Date;
import java.sql.SQLException;

import isep.lapr3.g094.repository.GestorAgriculaRepository;
import isep.lapr3.g094.repository.Repositories;

public class GestorAgriculaController {
    // Your code here
    public boolean registerOperation(char operationType, int value, int parcelaID, int plantacaoID, Date dataOperação)
            throws SQLException {
        GestorAgriculaRepository repository = Repositories.getInstance().getGestorAgriculaRepository();
        switch (Character.toUpperCase(operationType)) {
        case 'M':
            return repository.registerMonda(value, parcelaID, plantacaoID, dataOperação);
        case 'S':
            return repository.registerSemeadura(value, parcelaID, plantacaoID, dataOperação);
        case 'R':
            return repository.registerColheita(value, parcelaID, plantacaoID, dataOperação);
        case 'F':
            return repository.registerFatorDeProducao(value, parcelaID, plantacaoID, dataOperação);
        case 'P':
            return repository.registerPoda(value, parcelaID, plantacaoID, dataOperação);
        default:
            return false;
        }
    }
}