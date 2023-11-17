package isep.lapr3.g094.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import isep.lapr3.g094.application.controller.CreatePlanController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.irrigation.IrrigationSector;


public class IrrigationTest {

    private CreatePlanController controller;
    private ImportController importController;

    @BeforeEach
    void setUp() {
        importController = new ImportController();
        importController.importIrrigationPlan();
        controller = new CreatePlanController();
        controller.createPlan("01/12/2020");
    }

    @Test
    public void testSearchCheckOddDays() throws IOException, ParseException {

        Map<IrrigationSector, Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for (int i = 1; i < 30; i += 2) {

            output = controller.searchIrrigation(i + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getPeriodicidade())
                    .collect(Collectors.toSet());

            assertEquals(true, tiposRega.contains('I'));
            assertEquals(true, tiposRega.contains('T'));
        }
    }

    @Test
    public void testSearchCheckEvenDays() throws IOException, ParseException {

        controller.createPlan("01/12/2020");
        Map<IrrigationSector, Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for (int i = 2; i < 30; i += 2) {

            output = controller.searchIrrigation(i + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getPeriodicidade())
                    .collect(Collectors.toSet());

            assertEquals(true, tiposRega.contains('P'));
            assertEquals(true, tiposRega.contains('T'));
        }
    }

    @Test
    public void testSearchCheck3Days() throws IOException, ParseException {

        controller.createPlan("01/12/2020");
        Map<IrrigationSector, Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for (int i = 3; i < 30; i += 3) {

            output = controller.searchIrrigation(i + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getPeriodicidade())
                    .collect(Collectors.toSet());

            assertEquals(true, tiposRega.contains('3'));
            assertEquals(true, tiposRega.contains('T'));
        }
    }

    @Test
    public void testSearchCheck30PlusDays() throws IOException, ParseException {

        controller.createPlan("01/11/2020");
        Map<IrrigationSector, Integer> output;
        output = controller.searchIrrigation("25/12/2020", "8:31");
        assertNull(output);
    }

    @Test
    public void testSeachCheckHour() throws IOException, ParseException {

        controller.createPlan("01/12/2020");
        Map<IrrigationSector, Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for (int i = 2; i < 30; i += 2) {

            output = controller.searchIrrigation(i + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getPeriodicidade())
                    .collect(Collectors.toSet());

            assertEquals(true, tiposRega.contains('P'));
        }

    }

    @Test
    public void testSeachCheckNonExistentHours() throws IOException, ParseException {

        controller.createPlan("01/12/2020");
        Map<IrrigationSector, Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for (int i = 1; i < 30; i++) {
            output = controller.searchIrrigation(i + "/12/2020", "9:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getPeriodicidade())
                    .collect(Collectors.toSet());

            assertEquals(true, tiposRega.isEmpty());
        }

    }
}
