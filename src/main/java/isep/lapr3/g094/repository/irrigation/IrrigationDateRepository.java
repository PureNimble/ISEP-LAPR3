package isep.lapr3.g094.repository.irrigation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import isep.lapr3.g094.domain.irrigation.IrrigationDate;

public class IrrigationDateRepository {

    private List<IrrigationDate> irrigationDates = new ArrayList<>();

    public List<IrrigationDate> getIrrigationDates() {
        return irrigationDates;
    }

    public IrrigationDate getIrrigationDateByDate(Date date) {

        for (IrrigationDate irrigationDate : irrigationDates) {
            if (irrigationDate.getDate().equals(date)) {
                return irrigationDate;
            }
        }
        return null;
    }

    public Optional<IrrigationDate> createIrrigationDate(Date date) {

        Optional<IrrigationDate> optionalValue = Optional.empty();
        IrrigationDate irrigationDate = new IrrigationDate(date);

        if (addIrrigationDate(irrigationDate)) {
            optionalValue = Optional.of(irrigationDate);

        }
        return optionalValue;
    }

    private boolean addIrrigationDate(IrrigationDate irrigationDate) {
        boolean success = false;
        if (validateIrrigationDate(irrigationDate)) {
            success = irrigationDates.add(irrigationDate);
        }
        return success;
    }

    public void clearDate() {
        irrigationDates.clear();
    }

    private boolean validateIrrigationDate(IrrigationDate irrigationDate) {
        return !irrigationDates.contains(irrigationDate);
    }
}
