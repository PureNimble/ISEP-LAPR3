package isep.lapr3.g094.domain.irrigation;

public class IrrigationHour {

    String hour;

    public IrrigationHour(String hour) {
        this.hour = hour;
    }

    public String getHour() {
        return hour;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hour == null) ? 0 : hour.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IrrigationHour other = (IrrigationHour) obj;
        if (hour == null) {
            if (other.hour != null)
                return false;
        } else if (!hour.equals(other.hour))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Hora= " + hour;
    }
}
