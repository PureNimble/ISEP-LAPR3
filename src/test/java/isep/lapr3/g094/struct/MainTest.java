package isep.lapr3.g094.struct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.Location;
import isep.lapr3.g094.respository.LocationRepository;
import isep.lapr3.g094.respository.Repositories;
import isep.lapr3.g094.struct.graph.map.BasketDistribution;

public class MainTest {
    private List<String> distances;
    private List<Location> locations;
    private BasketDistribution basketDistribution;

    @BeforeEach
    void setUp() {
        ImportController importController = new ImportController();
        Repositories repositories = Repositories.getInstance();
        LocationRepository locationRepository = repositories.getLocationRepository();
        distances = importController.importDistances();
        importController.importLocations();

        locations = locationRepository.getLocations();

        basketDistribution = new BasketDistribution();
        for (Location location : locations) {
            basketDistribution.addLocation(location);
        }

        for (String distance : distances) {
            String[] line = distance.split(",");
            basketDistribution.addDistance(basketDistribution.locationById(line[0]),
                    basketDistribution.locationById(line[1]), Integer.parseInt(line[2]));
        }
    }

    @Test
    void testImportLocations() {
        assertEquals(323, locations.size());
    }

    @Test
    void testImportDistances() {
        assertEquals(783, distances.size());
    }

    @Test
    void testBasketDistribution() {
        System.out.println("Número de Localizações(Vértices): " + basketDistribution.getNumLocations());
        System.out.println("Número de Distâncias(Arestas): " + basketDistribution.getNumDistances());
        System.out.println(basketDistribution.toString());
    }

    @Test
    void testBasketDistributionLocationsNum() {
        System.out.println("Testing BasketDistributionLocationsNum...");

        assertEquals(323, basketDistribution.getNumLocations());
    }

    @Test
    void testBasketDistributionDistancesNum() {
        System.out.println("Testing BasketDistributionDistancesNum...");

        assertEquals(1566, basketDistribution.getNumDistances());
    }

    @Test
    void testBasketDistributionNullLocation() {
        System.out.println("Testing BasketDistributionNullLocation...");

        assertThrows(RuntimeException.class, () -> basketDistribution.addLocation(null));
        assertThrows(RuntimeException.class, () -> basketDistribution.addDistance(null, null, 0));
    }

    @Test
    void testBasketDistributionLocationByKey() {
        System.out.println("Testing BasketDistributionLocationByKey...");

        assertEquals("CT43", basketDistribution.locationByKey(0).getId());
        assertEquals("CT268", basketDistribution.locationByKey(12).getId());
        assertEquals("CT137", basketDistribution.locationByKey(232).getId());
        assertEquals("CT159", basketDistribution.locationByKey(322).getId());
        assertNull(basketDistribution.locationByKey(999));
    }

    @Test
    void testBasketDistributionKeyByLocation() {
        System.out.println("Testing BasketDistributionKeyLocation...");

        assertEquals(0, basketDistribution.keyLocation(locations.stream()
                .filter(l -> "CT43".equals(l.getId())).findFirst().orElse(null)));
        assertEquals(12, basketDistribution.keyLocation(locations.stream()
                .filter(l -> "CT268".equals(l.getId())).findFirst().orElse(null)));
        assertEquals(232, basketDistribution.keyLocation(locations.stream()
                .filter(l -> "CT137".equals(l.getId())).findFirst().orElse(null)));
        assertEquals(322, basketDistribution.keyLocation(locations.stream()
                .filter(l -> "CT159".equals(l.getId())).findFirst().orElse(null)));
        assertEquals(-1, basketDistribution.keyLocation(null));
    }

    @Test
    void testBasketDistributionLocationByID() {
        System.out.println("Testing BasketDistributionLocationByID...");

        Location location1 = new Location("CT123", 0, 0, 0);
        Location location2 = new Location("CT268", 0, 0, 0);
        basketDistribution.addLocation(location1);
        basketDistribution.addLocation(location2);

        assertEquals(location1, basketDistribution.locationById("CT123"));
        assertEquals(location2, basketDistribution.locationById("CT268"));
        assertNull(basketDistribution.locationById("CT999"));
    }
}