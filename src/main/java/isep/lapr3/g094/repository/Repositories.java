package isep.lapr3.g094.repository;

import isep.lapr3.g094.repository.irrigation.IrrigationDateRepository;
import isep.lapr3.g094.repository.irrigation.IrrigationHourRepository;
import isep.lapr3.g094.repository.irrigation.IrrigationSectorRepository;

public class Repositories {

    private static final Repositories instance = new Repositories();
    private GraphRepository graphRepository;
    private IrrigationSectorRepository irrigationSectorRepository;
    private IrrigationDateRepository irrigationDateRepository;
    private IrrigationHourRepository irrigationHourRepository;
    private FarmManagerRepository farmManagerRepository;

    public Repositories() {
        irrigationSectorRepository = new IrrigationSectorRepository();
        irrigationDateRepository = new IrrigationDateRepository();
        irrigationHourRepository = new IrrigationHourRepository();
        farmManagerRepository = new FarmManagerRepository();
        graphRepository = new GraphRepository();

    }

    public static Repositories getInstance() {
        return instance;
    }

    public IrrigationSectorRepository getIrrigationSectorRepository() {
        return irrigationSectorRepository;
    }

    public IrrigationDateRepository getIrrigationDateRepository() {
        return irrigationDateRepository;
    }

    public IrrigationHourRepository getIrrigationHourRepository() {
        return irrigationHourRepository;
    }

    public FarmManagerRepository getFarmManagerRepository() {
        return farmManagerRepository;
    }

    public GraphRepository getGraphRepository() {
        return graphRepository;
    }

}
