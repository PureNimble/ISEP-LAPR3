package isep.lapr3.g094.struct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import isep.lapr3.g094.domain.Location;
import isep.lapr3.g094.imports.Import;
import isep.lapr3.g094.struct.graph.map.BasketDistribution;

public class MainTest {
    private Import imports;
    private List<String> distances;
    private List<Location> locations;
    private BasketDistribution basketDistribution;

    @BeforeEach
    void setUp() {
        imports = new Import();
        distances = imports.importDistances();
        locations = imports.importLocations();
        basketDistribution = new BasketDistribution();
    }

    @Test
    void testImportDistances() {
        assertEquals(imports.importDistances().size(), 33);
    }

    @Test
    void testImportLocations() {
        assertEquals(imports.importLocations().size(), 17);
    }

    @Test
    void testBasketDistribution() {
        for (Location location : locations) {
            basketDistribution.addLocation(location);
        }

        for (String distance : distances) {
            String[] line = distance.split(",");
            basketDistribution.addDistance(basketDistribution.locationById(line[0]),
                    basketDistribution.locationById(line[1]), Integer.parseInt(line[2]));
        }

        System.out.println("Número de Localizações(Vértices): " + basketDistribution.getNumLocations());
        System.out.println("Número de Distâncias(Arestas): " + basketDistribution.getNumDistances());
        System.out.println(basketDistribution.toString());
    }
}