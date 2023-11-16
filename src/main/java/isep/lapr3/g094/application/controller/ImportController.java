package isep.lapr3.g094.application.controller;

import java.util.List;

import isep.lapr3.g094.domain.Location;
import isep.lapr3.g094.imports.Import;
import isep.lapr3.g094.respository.LocationRepository;
import isep.lapr3.g094.respository.Repositories;

public class ImportController {

    Import importClass = new Import();
    private LocationRepository locationRepository;

    public ImportController() {
        getLocationRepository();
    }

    private LocationRepository getLocationRepository() {
        if (locationRepository == null) {
            Repositories repositories = Repositories.getInstance();

            locationRepository = repositories.getLocationRepository();
        }
        return locationRepository;
    }

    public List<Location> importLocations() {
        List<String> locations = importClass.importFile("esinf/locais_big.csv");
        for (String location : locations) {
            String[] line = location.split(",");
            locationRepository.createLocations(line[0], Double.parseDouble(line[1].replace(',', '.')),
            Double.parseDouble(line[2].replace(',', '.')), Integer.parseInt(line[0].substring(2)));
        }
        return locationRepository.getLocations();
    }

    public List<String> importDistances() {
        return importClass.importFile("esinf/distancias_big.csv");
    }
}
