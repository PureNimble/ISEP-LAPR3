package isep.lapr3.g094.application.controller;

import java.io.IOException;
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
        irrigationHourRepository.clear();
        irrigationSectorRepository.clear();

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
                if (line.length == 3) {
                    if (irrigationSectorRepository
                            .createIrrigationSectors(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                                    line[2].charAt(0), null, null)
                            .isEmpty()) {
                        return false;
                    }
                } else if (line.length >= 5) {
                    if (irrigationSectorRepository
                            .createIrrigationSectors(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                                    line[2].charAt(0), Integer.parseInt(line[3]), Integer.parseInt(line[4]))
                            .isEmpty()) {
                        return false;
                    }
                }
            } catch (NumberFormatException e) {
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

    public boolean importToGraph(boolean bigGraph) {
        boolean check = true;
        String locais;
        String distancias;

        locais = bigGraph ? "esinf/locais_big.csv" : "esinf/locais_small.csv";
        distancias = bigGraph ? "esinf/distancias_big.csv" : "esinf/distancias_small.csv";

        List<String> locations = importClass.importTxtFile(locais, true);
        List<String> distances = importClass.importTxtFile(distancias, true);
        if (locations.isEmpty() || distances.isEmpty())
            return false;
        if (!service.createLocation(locations, bigGraph) || !service.addDistance(distances, bigGraph))
            check = false;
        return check;
    }

    public boolean importOpeningHours(String path, boolean bigGraph) {
        String openingHours = path;
        List<String> horarios = importClass.importTxtFile(openingHours, true);
        if (horarios.isEmpty())
            return false;
        try {
            return service.createOpeningHours(horarios, bigGraph);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<String> getFilesFromDirectory(String string) throws IOException {
        return importClass.importFilesNames(string);
    }
}
