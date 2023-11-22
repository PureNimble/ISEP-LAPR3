package isep.lapr3.g094.application.controller;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import isep.lapr3.g094.domain.imports.Import;
import isep.lapr3.g094.repository.Repositories;
import isep.lapr3.g094.repository.irrigation.IrrigationHourRepository;
import isep.lapr3.g094.repository.irrigation.IrrigationSectorRepository;
import isep.lapr3.g094.services.Service;
import isep.lapr3.g094.services.Services;

public class ImportController {

    private Import importClass;
    private Service service;
    private IrrigationSectorRepository irrigationSectorRepository;
    private IrrigationHourRepository irrigationHourRepository;

    public ImportController() {
        importClass = new Import();
        getService();
        getIrrigationSectorRepository();
        getIrrigationHourRepository();
    }

    private Service getService() {
        if (service == null) {
            Services services = Services.getInstance();

            service = services.getService();
        }
        return service;
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

     public List<String> importBddadNewData() {
        return importClass.importTxtFile("bddad/NovosDados.txt", false);
    }

    public boolean importToGraph() {
        boolean check = true;
        List<String> locations = importClass.importTxtFile("esinf/locais_big.csv", true);
        List<String> distances = importClass.importTxtFile("esinf/distancias_big.csv", true);
        if (locations.isEmpty() || distances.isEmpty())
            return false;
        if(!service.createLocation(locations) || !service.addDistance(distances)) check = false;
        return check;
    }
    
}
