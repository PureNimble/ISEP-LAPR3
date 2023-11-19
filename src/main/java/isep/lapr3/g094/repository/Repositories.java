package isep.lapr3.g094.repository;

import isep.lapr3.g094.repository.irrigation.IrrigationDateRepository;
import isep.lapr3.g094.repository.irrigation.IrrigationHourRepository;
import isep.lapr3.g094.repository.irrigation.IrrigationSectorRepository;

public class Repositories {

    private static final Repositories instance = new Repositories();
    private LocationRepository locationRepository;
    private IrrigationSectorRepository irrigationSectorRepository;
    private IrrigationDateRepository irrigationDateRepository;
    private IrrigationHourRepository irrigationHourRepository;
    private GestorAgriculaRepository gestorAgriculaRepository;

    public Repositories() {
        locationRepository = new LocationRepository();
        irrigationSectorRepository = new IrrigationSectorRepository();
        irrigationDateRepository = new IrrigationDateRepository();
        irrigationHourRepository = new IrrigationHourRepository();
        gestorAgriculaRepository = new GestorAgriculaRepository();

    }

    public static Repositories getInstance() {
        return instance;
    }

    public LocationRepository getLocationRepository() {
        return locationRepository;
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

    public GestorAgriculaRepository getGestorAgriculaRepository() {
        return gestorAgriculaRepository;
    }
}
