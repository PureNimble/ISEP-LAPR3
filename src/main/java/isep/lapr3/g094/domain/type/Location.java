package isep.lapr3.g094.domain.type;

import java.util.Objects;

public class Location {

    private String id;
    private double latitude;
    private double longitude;
    private int numEmployees;

    public Location(String id, double latitude, double longitude, int numEmployees) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numEmployees = numEmployees;
    }

    public Location(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getNumEmployees() {
        return numEmployees;
    }

    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Localização [id= " + id + ", latitude= " + latitude + ", longitude= " + longitude
                + ", Número de empregados= "
                + numEmployees + "]";
    }
}
