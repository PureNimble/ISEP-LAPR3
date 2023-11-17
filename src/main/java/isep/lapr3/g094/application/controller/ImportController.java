package isep.lapr3.g094.application.controller;

import java.util.Iterator;
import java.util.List;

import isep.lapr3.g094.imports.Import;
import isep.lapr3.g094.respository.IrrigationHourRepository;
import isep.lapr3.g094.respository.IrrigationSectorRepository;
import isep.lapr3.g094.respository.LocationRepository;
import isep.lapr3.g094.respository.Repositories;

public class ImportController {

    Import importClass = new Import();
    private LocationRepository locationRepository;
    private IrrigationSectorRepository irrigationSectorRepository;
    private IrrigationHourRepository irrigationHourRepository;

    public ImportController() {
        getLocationRepository();
        getIrrigationSectorRepository();
        getIrrigationHourRepository();
    }

    private LocationRepository getLocationRepository() {
        if (locationRepository == null) {
            Repositories repositories = Repositories.getInstance();

            locationRepository = repositories.getLocationRepository();
        }
        return locationRepository;
    }

    private IrrigationSectorRepository getIrrigationSectorRepository() {
        if (irrigationSectorRepository == null) {
            Repositories repositories = Repositories.getInstance();

            irrigationSectorRepository = repositories.getIrrigationSectorRepository();
        }
        return irrigationSectorRepository;
    }

    private IrrigationHourRepository getIrrigationHourRepository() {
        if (irrigationHourRepository == null) {
            Repositories repositories = Repositories.getInstance();

            irrigationHourRepository = repositories.getIrrigationHourRepository();
        }
        return irrigationHourRepository;
    }

    public void importLocations() {
        List<String> locations = importClass.importFile("esinf/locais_big.csv", true);
        for (String location : locations) {
            String[] line = location.split(",");
            locationRepository.createLocations(line[0], Double.parseDouble(line[1].replace(',', '.')),
                    Double.parseDouble(line[2].replace(',', '.')), Integer.parseInt(line[0].substring(2)));
        }
    }

    public void importIrrigationPlan() {

        List<String> plan = importClass.importFile("lapr3/PlanoDeRega.txt", false);
        Iterator<String> iterator = plan.iterator();

        if (iterator.hasNext()) {
            String[] line = iterator.next().split(",");
            for (String element : line) {
                if (element.length() != 5) {
                    element = "0" + element;
                }
                irrigationHourRepository.createIrrigationHour(element);
            }
        }

        while (iterator.hasNext()) {
            String[] line = iterator.next().split(",");
            irrigationSectorRepository.createIrrigationSectors(line[0].charAt(0), Integer.parseInt(line[1]),
                    line[2].charAt(0));
        }
    }

    public List<String> importDistances() {
        return importClass.importFile("esinf/distancias_big.csv", true);
    }
}
