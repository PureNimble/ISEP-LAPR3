package isep.lapr3.g094.services;

public class Services {

    private static final Services instance = new Services();
    private Service service;

    public Services() {
        service = new Service();
    }

    public static Services getInstance() {
        return instance;
    }

    public Service getService() {
        return service;
    }
}