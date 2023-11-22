package isep.lapr3.g094.repository.irrigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import isep.lapr3.g094.domain.irrigation.IrrigationHour;

public class IrrigationHourRepository {
    private List<IrrigationHour> irrigationHours = new ArrayList<>();

    public List<IrrigationHour> getIrrigationHours() {
        return irrigationHours;
    }

    public IrrigationHour getIrrigationHourByHour(String hour) {

        for (IrrigationHour irrigationHour : irrigationHours) {
            if (irrigationHour.getHour().equals(hour)) {
                return irrigationHour;
            }
        }
        return null;
    }

    public Optional<IrrigationHour> createIrrigationHour(String hour) {

        Optional<IrrigationHour> optionalValue = Optional.empty();
        IrrigationHour irrigationHour = new IrrigationHour(hour);

        if (addIrrigationHour(irrigationHour)) {
            optionalValue = Optional.of(irrigationHour);

        }
        return optionalValue;
    }

    private boolean addIrrigationHour(IrrigationHour irrigationHour) {
        boolean success = false;
        if (validateIrrigationHour(irrigationHour)) {
            success = irrigationHours.add(irrigationHour);
        }
        return success;
    }

    public void clear() {
        irrigationHours.clear();
    }

    private boolean validateIrrigationHour(IrrigationHour irrigationHour) {
        return !irrigationHours.contains(irrigationHour);
    }
}
