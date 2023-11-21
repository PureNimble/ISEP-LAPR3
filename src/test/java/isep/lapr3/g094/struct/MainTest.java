package isep.lapr3.g094.struct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import isep.lapr3.g094.application.controller.GraphController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.repository.GraphRepository;
import isep.lapr3.g094.repository.Repositories;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainTest {

    private GraphController graphController;
    private GraphRepository graphRepository;

    @BeforeEach
    void setUp() {
        Repositories repositories = Repositories.getInstance();
        graphRepository = repositories.getGraphRepository();
        ImportController importController = new ImportController();
        importController.importToGraph();
        graphController = new GraphController();
    }

    @Test
    void testBasketDistributionLocationsNum() {
        System.out.println("Testing BasketDistributionLocationsNum...");

        assertEquals(323, graphController.getNumLocations());
    }

    @Test
    void testBasketDistributionDistancesNum() {
        System.out.println("Testing BasketDistributionDistancesNum...");

        assertEquals(1566, graphController.getNumDistances());
    }

    @Test
    void testBasketDistributionNullLocation() {
        System.out.println("Testing BasketDistributionNullLocation...");

        assertThrows(RuntimeException.class, () -> graphRepository.addLocation(null));
        assertThrows(RuntimeException.class, () -> graphRepository.addDistance(null, null, 0));
    }

    @Test
    void testBasketDistributionLocationByKey() {
        System.out.println("Testing BasketDistributionLocationByKey...");

        assertEquals("CT43", graphController.locationByKey(0).getId());
        assertEquals("CT268", graphController.locationByKey(12).getId());
        assertEquals("CT137", graphController.locationByKey(232).getId());
        assertEquals("CT159", graphController.locationByKey(322).getId());
        assertNull(graphController.locationByKey(999));
    }

    @Test
    void testBasketDistributionKeyByLocation() {
        System.out.println("Testing BasketDistributionKeyLocation...");

        assertEquals(0, graphController.keyLocation("CT43"));
        assertEquals(123, graphController.keyLocation("CT305"));
        assertEquals(169, graphController.keyLocation("CT195"));
        assertEquals(222, graphController.keyLocation("CT201"));
        assertEquals(269, graphController.keyLocation("CT318"));
        assertEquals(322, graphController.keyLocation("CT159"));

    }

    @Test
    void testBasketDistributionLocationByID() {
        System.out.println("Testing BasketDistributionLocationByID...");

        Location location1 = new Location("CT123", 0, 0, 0);
        Location location2 = new Location("CT268", 0, 0, 0);
        graphRepository.addLocation(location1);
        graphRepository.addLocation(location2);

        assertEquals(location1, graphController.locationById("CT123"));
        assertEquals(location2, graphController.locationById("CT268"));
        assertNull(graphController.locationById("CT999"));
    }

    @Test
    void testShortestPath() {
        System.out.println("Testing ShortestPath...");

        LinkedList<Location> shortPath = new LinkedList<>();
        assertEquals(147618, graphController.shortestPath("CT1", "CT3", shortPath));
        List<String> expected1 = Arrays.asList("CT1", "CT256", "CT301", "CT262", "CT208", "CT76", "CT166", "CT90",
                "CT288", "CT160", "CT32", "CT3");
        for (Location location : shortPath) {
            assertTrue(expected1.contains(location.getId()));
        }

        shortPath.clear();
        assertEquals(45417, graphController.shortestPath("CT13", "CT232", shortPath));
        List<String> expected2 = Arrays.asList("CT13", "CT44", "CT144", "CT191", "CT109", "CT28", "CT232");
        for (Location location : shortPath) {
            assertTrue(expected2.contains(location.getId()));
        }

        shortPath.clear();
        assertEquals(255582, graphController.shortestPath("CT16", "CT200", shortPath));
        List<String> expected3 = Arrays.asList("CT16", "CT273", "CT170", "CT311", "CT257", "CT153", "CT68", "CT72",
                "CT246", "CT181", "CT142", "CT90", "CT91", "CT270", "CT219", "CT70", "CT200");
        for (Location location : shortPath) {
            assertTrue(expected3.contains(location.getId()));
        }

        shortPath.clear();
        assertEquals(27700, graphController.shortestPath("CT100", "CT200", shortPath));
        List<String> expected4 = Arrays.asList("CT100", "CT281", "CT200");
        for (Location location : shortPath) {
            assertTrue(expected4.contains(location.getId()));
        }

        shortPath.clear();
        assertEquals(265601, graphController.shortestPath("CT1", "CT322", shortPath));
        List<String> expected5 = Arrays.asList("CT1", "CT256", "CT301", "CT262", "CT208", "CT76", "CT166", "CT90",
                "CT288", "CT160", "CT32", "CT3", "CT286", "CT265", "CT292", "CT41", "CT78", "CT210", "CT147", "CT113",
                "CT322");
        for (Location location : shortPath) {
            assertTrue(expected5.contains(location.getId()));
        }
    }

    @Test
    void testLocationDegree() {
        System.out.println("Testing LocationDegree...");

        assertEquals(5, graphController.getLocationDegree("CT1"));
        assertEquals(6, graphController.getLocationDegree("CT132"));
        assertEquals(7, graphController.getLocationDegree("CT154"));
        assertEquals(3, graphController.getLocationDegree("CT321"));
    }

    @Test
    void testShortestPaths() {
        System.out.println("Testing ShortestPaths...");

        assertTrue(graphController.shortestPaths("CT1"));
    }
}