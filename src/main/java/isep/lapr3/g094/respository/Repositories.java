package isep.lapr3.g094.respository;

public class Repositories {

    private static final Repositories instance = new Repositories();
    private LocationRepository locationRepository = new LocationRepository();
    private IrrigationSectorRepository irrigationSectorRepository = new IrrigationSectorRepository();
    private IrrigationDateRepository IrrigationDateRepository = new IrrigationDateRepository();
    private IrrigationHourRepository IrrigationHourRepository = new IrrigationHourRepository();

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
        return IrrigationDateRepository;
    }

    public IrrigationHourRepository getIrrigationHourRepository() {
        return IrrigationHourRepository;
    }
}
