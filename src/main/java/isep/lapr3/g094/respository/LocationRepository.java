package isep.lapr3.g094.respository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import isep.lapr3.g094.domain.Location;

public class LocationRepository {
    private List<Location> locations = new ArrayList<>();

    public List<Location> getLocations() {
        return locations;
    }

    public Location getLocationById(String id) {

        for (Location location : locations) {
            if (location.getId().equals(id)) {
                return location;
            }
        }
        return null;
    }

    public Optional<Location> createLocations(String id, double latitude, double longitude, int numEmployees) {

        Optional<Location> optionalValue = Optional.empty();
        Location location = new Location(id, latitude, longitude, numEmployees);

        if (addLocation(location)) {
            optionalValue = Optional.of(location);

        }
        return optionalValue;
    }

    private boolean addLocation(Location location) {
        boolean success = false;
        if (validateLocations(location)) {
            success = locations.add(location);
        }
        return success;
    }

    private boolean validateLocations(Location location) {
        return !locations.contains(location);
    }
}
