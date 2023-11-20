package isep.lapr3.g094.application.controller;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import isep.lapr3.g094.imports.Import;
import isep.lapr3.g094.repository.LocationRepository;
import isep.lapr3.g094.repository.Repositories;
import isep.lapr3.g094.repository.irrigation.IrrigationHourRepository;
import isep.lapr3.g094.repository.irrigation.IrrigationSectorRepository;

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

    public boolean importLocations() {
        List<String> locations = importClass.importTxtFile("esinf/locais_big.csv", true);
        if (locations.isEmpty())
            return false;
        for (String location : locations) {
            String[] line = location.split(",");
            try {
                if (locationRepository.createLocations(line[0], Double.parseDouble(line[1].replace(',', '.')),
                        Double.parseDouble(line[2].replace(',', '.')), Integer.parseInt(line[0].substring(2)))
                        .isEmpty()) {
                    return false;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
    }

    public boolean importIrrigationPlan() {

        List<String> plan = importClass.importTxtFile("lapr3/PlanoDeRega.txt", false);
        if (plan.isEmpty())
            return false;
        Iterator<String> iterator = plan.iterator();

        if (iterator.hasNext()) {
            String[] line = iterator.next().split(",");
            for (String element : line) {
                if (element.length() != 5) {
                    element = "0" + element;
                }
                if (irrigationHourRepository.createIrrigationHour(element).isEmpty())
                    return false;
            }
        }

        while (iterator.hasNext()) {
            String[] line = iterator.next().split(",");
            try {
                if (irrigationSectorRepository.createIrrigationSectors(line[0].charAt(0), Integer.parseInt(line[1]),
                        line[2].charAt(0)).isEmpty()) {
                    return false;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
    }

    public List<LinkedHashSet<List<String>>> importXlsx() {
        return importClass.importXlsxFile("bddad/Legacy_Data_v2a.xlsx");
    }

    public List<String> importDistances() {
        return importClass.importTxtFile("esinf/distancias_big.csv", true);
    }
}
