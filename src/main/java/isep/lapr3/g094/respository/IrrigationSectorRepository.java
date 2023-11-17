package isep.lapr3.g094.respository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import isep.lapr3.g094.domain.IrrigationSector;

public class IrrigationSectorRepository {

    private List<IrrigationSector> irrigationSectors = new ArrayList<>();

    public List<IrrigationSector> getIrrigationSectors() {
        return irrigationSectors;
    }

    public IrrigationSector getIrrigationBySector(char sector) {

        for (IrrigationSector irrigationSector : irrigationSectors) {
            if (irrigationSector.getSector() == sector) {
                return irrigationSector;
            }
        }
        return null;
    }

    public Optional<IrrigationSector> createIrrigationSectors(char sector, int duracao, char periocidade) {

        Optional<IrrigationSector> optionalValue = Optional.empty();
        IrrigationSector irrigationSector = new IrrigationSector(sector, duracao, periocidade);

        if (addIrrigationSector(irrigationSector)) {
            optionalValue = Optional.of(irrigationSector);

        }
        return optionalValue;
    }

    private boolean addIrrigationSector(IrrigationSector irrigationSector) {
        boolean success = false;
        if (validateIrrigationSector(irrigationSector)) {
            success = irrigationSectors.add(irrigationSector);
        }
        return success;
    }

    private boolean validateIrrigationSector(IrrigationSector irrigationSector) {
        return !irrigationSectors.contains(irrigationSector);
    }
}
