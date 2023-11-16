package isep.lapr3.g094.respository;

public class Repositories {

    private static final Repositories instance = new Repositories();
    private LocationRepository locationRepository = new LocationRepository();

    private Repositories() {
    }

    public static Repositories getInstance() {
        return instance;
    }

    public LocationRepository getLocationRepository() {
        return locationRepository;
    }
}
