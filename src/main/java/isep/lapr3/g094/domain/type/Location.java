package isep.lapr3.g094.domain.type;

import java.time.LocalTime;
import java.util.Objects;

public class Location {

    private String id;
    private double latitude;
    private double longitude;
    private int numEmployees;
    private LocalTime startHour;
    private LocalTime endHour;
    private boolean isHub;

    public Location(String id, double latitude, double longitude, int numEmployees, LocalTime startHour, LocalTime endHour) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numEmployees = numEmployees;
        this.startHour = startHour;
        this.endHour = endHour;
        this.isHub = false;
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

    public LocalTime getStartHour() {
        return startHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public void setStarHour(LocalTime localTime) {
        this.startHour = localTime;
    }

    public void setEndHour(LocalTime localTime) {
        this.endHour = localTime;
    }

    public boolean isHub() {
        return isHub;
    }

    public void setHub(boolean hub) {
        isHub = hub;
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
                + ", Número de empregados= " + numEmployees 
                + ", hora de início = " + (startHour != null ? startHour.toString() : "N/A")
                + ", hora de fecho = " + (endHour != null ? endHour.toString() : "N/A") + "]"
                + ", hub? " + (isHub ? "Sim" : "Não")  + "]";
    }
}
