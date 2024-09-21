package isep.lapr3.g094.domain.irrigation;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IrrigationDate {

    Date date;

    public IrrigationDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
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
        IrrigationDate other = (IrrigationDate) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Data= " + new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

}
