package isep.lapr3.g094.struct;

import isep.lapr3.g094.struct.graph.Graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import isep.lapr3.g094.application.controller.GraphController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.type.Criteria;
import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.repository.GraphRepository;
import isep.lapr3.g094.repository.Repositories;
import isep.lapr3.g094.services.Service;
import isep.lapr3.g094.services.Services;
import isep.lapr3.g094.struct.graph.Algorithms;
import isep.lapr3.g094.struct.graph.map.MapGraph;

public class MainTest {

    private GraphController graphController;
    private GraphRepository graphRepository;
    private Service service;
    private MapGraph<Location, Integer> BASKET_DISTRIBUTION = new MapGraph<>(false);

    @BeforeEach
    void setUp() {
        Repositories repositories = Repositories.getInstance();
        Services services = Services.getInstance();
        graphRepository = repositories.getGraphRepository();
        service = services.getService();
        ImportController importController = new ImportController();
        importController.importToGraph();
        graphController = new GraphController();
        BASKET_DISTRIBUTION = graphRepository.getBasketDistribution();
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

        assertNull(service.locationByKey(1000000));
        assertEquals("CT43", service.locationByKey(0).getId());
        assertEquals("CT211", service.locationByKey(69).getId());
        assertEquals("CT305", service.locationByKey(123).getId());
        assertEquals("CT253", service.locationByKey(265).getId());
        assertEquals("CT46", service.locationByKey(321).getId());
        assertEquals("CT159", service.locationByKey(322).getId());
        assertNull(service.locationByKey(999));
    }

    @Test
    void testBasketDistributionLocationById() {
        System.out.println("Testing BasketDistributionLocationById...");

        assertNull(service.locationById("CT999"));
        assertEquals(new Location("CT43"), service.locationById("CT43"));
        assertEquals(new Location("CT211"), service.locationById("CT211"));
        assertEquals(new Location("CT305"), service.locationById("CT305"));
        assertEquals(new Location("CT253"), service.locationById("CT253"));
        assertEquals(new Location("CT46"), service.locationById("CT46"));
        assertEquals(new Location("CT159"), service.locationById("CT159"));
    }

    @Test
    void testBasketDistributionKeyLocation() {
        System.out.println("Testing BasketDistributionKeyLocation...");

        assertEquals(-1, service.keyLocation(new Location("CT999")));
        assertEquals(0, service.keyLocation(new Location("CT43")));
        assertEquals(69, service.keyLocation(new Location("CT211")));
        assertEquals(123, service.keyLocation(new Location("CT305")));
        assertEquals(265, service.keyLocation(new Location("CT253")));
        assertEquals(321, service.keyLocation(new Location("CT46")));
        assertEquals(322, service.keyLocation(new Location("CT159")));
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
    void testBasketDistributionBreadthFirstSearch() {
        System.out.println("Testing BasketDistributionBreadthFirstSearch...");

        Assertions.assertNull(Algorithms.BreadthFirstSearch(BASKET_DISTRIBUTION, new Location("CT999")));

        LinkedList<Location> path = Algorithms.BreadthFirstSearch(BASKET_DISTRIBUTION, new Location("CT1"));
        assertEquals(323, path.size());

        assertEquals(new Location("CT1"), path.peekFirst());
        assertEquals(new Location("CT172"), path.peekLast());

        path = Algorithms.BreadthFirstSearch(BASKET_DISTRIBUTION, new Location("CT69"));
        assertEquals(new Location("CT69"), path.peekFirst());
        assertEquals(new Location("CT7"), path.peekLast());

        path = Algorithms.BreadthFirstSearch(BASKET_DISTRIBUTION, new Location("CT180"));
        assertEquals(new Location("CT180"), path.peekFirst());
        assertEquals(new Location("CT172"), path.peekLast());

        path = Algorithms.BreadthFirstSearch(BASKET_DISTRIBUTION, new Location("CT269"));
        assertEquals(new Location("CT269"), path.peekFirst());
        assertEquals(new Location("CT172"), path.peekLast());

        path = Algorithms.BreadthFirstSearch(BASKET_DISTRIBUTION, new Location("CT322"));
        assertEquals(new Location("CT322"), path.peekFirst());
        assertEquals(new Location("CT75"), path.peekLast());
    }

    @Test
    void testBasketDistributionDepthFirstSearch() {
        System.out.println("Testing BasketDistributionDepthFirstSearch...");

        assertNull(Algorithms.DepthFirstSearch(BASKET_DISTRIBUTION, new Location("CT999")));

        LinkedList<Location> path = Algorithms.DepthFirstSearch(BASKET_DISTRIBUTION, new Location("CT120"));
        assertEquals(323, path.size());

        assertEquals(new Location("CT120"), path.peekFirst());
        assertEquals(new Location("CT178"), path.peekLast());

        path = Algorithms.DepthFirstSearch(BASKET_DISTRIBUTION, new Location("CT69"));
        assertEquals(new Location("CT69"), path.peekFirst());
        assertEquals(new Location("CT265"), path.peekLast());

        path = Algorithms.DepthFirstSearch(BASKET_DISTRIBUTION, new Location("CT180"));
        assertEquals(new Location("CT180"), path.peekFirst());
        assertEquals(new Location("CT18"), path.peekLast());

        path = Algorithms.DepthFirstSearch(BASKET_DISTRIBUTION, new Location("CT269"));
        assertEquals(new Location("CT269"), path.peekFirst());
        assertEquals(new Location("CT186"), path.peekLast());

        path = Algorithms.DepthFirstSearch(BASKET_DISTRIBUTION, new Location("CT322"));
        assertEquals(new Location("CT322"), path.peekFirst());
        assertEquals(new Location("CT14"), path.peekLast());
    }

    @Test
    void testBasketDistributionAllPaths() {
        System.out.println("Testing BasketDistributionAllPaths...");

        MapGraph<Location, Integer> testGraph = new MapGraph<>(false);
        testGraph.addVertex(new Location("CT1"));
        testGraph.addVertex(new Location("CT2"));
        testGraph.addVertex(new Location("CT3"));
        testGraph.addVertex(new Location("CT4"));
        testGraph.addVertex(new Location("CT5"));

        testGraph.addEdge(new Location("CT1"), new Location("CT2"), 1);
        testGraph.addEdge(new Location("CT1"), new Location("CT3"), 1);
        testGraph.addEdge(new Location("CT2"), new Location("CT3"), 1);
        testGraph.addEdge(new Location("CT2"), new Location("CT4"), 1);
        testGraph.addEdge(new Location("CT3"), new Location("CT4"), 1);
        testGraph.addEdge(new Location("CT3"), new Location("CT5"), 1);
        testGraph.addEdge(new Location("CT4"), new Location("CT5"), 1);

        ArrayList<LinkedList<Location>> paths = Algorithms.allPaths(testGraph, new Location("CT1"), new Location("CT5"));
        assertEquals(7, paths.size());

        List<String> expected1 = Arrays.asList("CT1", "CT2", "CT3", "CT4", "CT5");
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), paths.get(0).get(i).getId());
        }

        List<String> expected2 = Arrays.asList("CT1", "CT2", "CT3", "CT5");
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), paths.get(1).get(i).getId());
        }

        List<String> expected3 = Arrays.asList("CT1", "CT2", "CT4", "CT3");
        for (int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i), paths.get(2).get(i).getId());
        }

        List<String> expected4 = Arrays.asList("CT1", "CT2", "CT4", "CT5");
        for (int i = 0; i < expected4.size(); i++) {
            assertEquals(expected4.get(i), paths.get(3).get(i).getId());
        }

        List<String> expected5 = Arrays.asList("CT1", "CT3", "CT2", "CT4", "CT5");
        for (int i = 0; i < expected5.size(); i++) {
            assertEquals(expected5.get(i), paths.get(4).get(i).getId());
        }
    }

    @Test
    void testShortestPath() {
        System.out.println("Testing ShortestPath...");

        LinkedList<Location> shortPath = new LinkedList<>();
        assertEquals(147618, Algorithms.shortestPath(BASKET_DISTRIBUTION, new Location("CT1"), new Location("CT3"),
                Integer::compare, Integer::sum, 0, shortPath));
        List<String> expected1 = Arrays.asList("CT1", "CT256", "CT301", "CT262", "CT208", "CT76", "CT166", "CT90",
                "CT288", "CT160", "CT32", "CT3");
        for (Location location : shortPath) {
            assertTrue(expected1.contains(location.getId()));
        }

        shortPath.clear();
        assertEquals(45417, Algorithms.shortestPath(BASKET_DISTRIBUTION, new Location("CT13"), new Location("CT232"),
                Integer::compare, Integer::sum, 0, shortPath));
        List<String> expected2 = Arrays.asList("CT13", "CT44", "CT144", "CT191", "CT109", "CT28", "CT232");
        for (Location location : shortPath) {
            assertTrue(expected2.contains(location.getId()));
        }

        shortPath.clear();
        assertEquals(255582, Algorithms.shortestPath(BASKET_DISTRIBUTION, new Location("CT16"), new Location("CT200"),
                Integer::compare, Integer::sum, 0, shortPath));
        List<String> expected3 = Arrays.asList("CT16", "CT273", "CT170", "CT311", "CT257", "CT153", "CT68", "CT72",
                "CT246", "CT181", "CT142", "CT90", "CT91", "CT270", "CT219", "CT70", "CT200");
        for (Location location : shortPath) {
            assertTrue(expected3.contains(location.getId()));
        }
        for (Location location : shortPath) {
            assertTrue(expected3.contains(location.getId()));
        }

        shortPath.clear();
        assertEquals(27700, Algorithms.shortestPath(BASKET_DISTRIBUTION, new Location("CT100"), new Location("CT200"),
                Integer::compare, Integer::sum, 0, shortPath));
        List<String> expected4 = Arrays.asList("CT100", "CT281", "CT200");
        for (Location location : shortPath) {
            assertTrue(expected4.contains(location.getId()));
        }

        shortPath.clear();
        assertEquals(265601, Algorithms.shortestPath(BASKET_DISTRIBUTION, new Location("CT1"), new Location("CT322"),
                Integer::compare, Integer::sum, 0, shortPath));
        List<String> expected5 = Arrays.asList("CT1", "CT256", "CT301", "CT262", "CT208", "CT76", "CT166", "CT90",
                "CT288", "CT160", "CT32", "CT3", "CT286", "CT265", "CT292", "CT41", "CT78", "CT210", "CT147", "CT113",
                "CT322");
        for (Location location : shortPath) {
            assertTrue(expected5.contains(location.getId()));
        }
    }

    @Test
    void testLocationDegree() {
        System.out.println("Testing LocationInDegree and LocationoutDegree...");

        assertEquals(BASKET_DISTRIBUTION.outDegree(new Location("CT1")),
                BASKET_DISTRIBUTION.inDegree(new Location("CT1")));
        assertEquals(BASKET_DISTRIBUTION.outDegree(new Location("CT132")),
                BASKET_DISTRIBUTION.inDegree(new Location("CT132")));
        assertEquals(BASKET_DISTRIBUTION.outDegree(new Location("CT154")),
                BASKET_DISTRIBUTION.inDegree(new Location("CT154")));
        assertEquals(BASKET_DISTRIBUTION.outDegree(new Location("CT321")),
                BASKET_DISTRIBUTION.inDegree(new Location("CT321")));
    }

    @Test
    void testShortestPaths() {
        System.out.println("Testing ShortestPaths...");

        ArrayList<LinkedList<Location>> shortPaths = new ArrayList<>();
        ArrayList<Integer> dists = new ArrayList<>();

        assertTrue(Algorithms.shortestPaths(BASKET_DISTRIBUTION, new Location("CT1"), Integer::compare, Integer::sum, 0,
                shortPaths, dists));
        assertEquals(shortPaths.size(), dists.size());
        List<String> expected1 = Arrays.asList("CT1", "CT222", "CT25", "CT175", "CT43");
        for (Location location : shortPaths.get(0)) {
            assertTrue(expected1.contains(location.getId()));
        }
        assertEquals(111434, dists.get(BASKET_DISTRIBUTION.key(new Location("CT43"))));

        shortPaths.clear();
        dists.clear();

        assertTrue(Algorithms.shortestPaths(BASKET_DISTRIBUTION, new Location("CT100"), Integer::compare, Integer::sum,
                0, shortPaths, dists));
        assertEquals(shortPaths.size(), dists.size());
        List<String> expected2 = Arrays.asList("CT100", "CT306", "CT283", "CT222", "CT25", "CT175", "CT43");
        for (Location location : shortPaths.get(0)) {
            assertTrue(expected2.contains(location.getId()));
        }
        assertEquals(134917, dists.get(BASKET_DISTRIBUTION.key(new Location("CT43"))));

        shortPaths.clear();
        dists.clear();

        assertTrue(Algorithms.shortestPaths(BASKET_DISTRIBUTION, new Location("CT169"), Integer::compare, Integer::sum,
                0, shortPaths, dists));
        assertEquals(shortPaths.size(), dists.size());
        List<String> expected3 = new ArrayList<>(Arrays.asList("CT169", "CT122", "CT119", "CT21", "CT132", "CT55",
                "CT124", "CT77", "CT188", "CT79", "CT223", "CT43"));
        for (Location location : shortPaths.get(0)) {
            assertTrue(expected3.contains(location.getId()));
        }
        assertEquals(319073, dists.get(BASKET_DISTRIBUTION.key(new Location("CT43"))));

        shortPaths.clear();
        dists.clear();

        assertTrue(Algorithms.shortestPaths(BASKET_DISTRIBUTION, new Location("CT200"), Integer::compare, Integer::sum,
                0, shortPaths, dists));
        assertEquals(shortPaths.size(), dists.size());
        List<String> expected4 = Arrays.asList("CT200", "CT278", "CT92", "CT283", "CT222", "CT25", "CT175", "CT43");
        for (Location location : shortPaths.get(0)) {
            assertTrue(expected4.contains(location.getId()));
        }
        assertEquals(155507, dists.get(BASKET_DISTRIBUTION.key(new Location("CT43"))));

        shortPaths.clear();
        dists.clear();

        assertTrue(Algorithms.shortestPaths(BASKET_DISTRIBUTION, new Location("CT322"), Integer::compare, Integer::sum,
                0, shortPaths, dists));
        assertEquals(shortPaths.size(), dists.size());
        List<String> expected5 = Arrays.asList("CT322", "CT113", "CT29", "CT49", "CT236", "CT81", "CT311", "CT257",
                "CT211", "CT130", "CT154", "CT96", "CT124", "CT77", "CT188", "CT79", "CT223", "CT43");
        for (Location location : shortPaths.get(0)) {
            assertTrue(expected5.contains(location.getId()));
        }
        assertEquals(325334, dists.get(BASKET_DISTRIBUTION.key(new Location("CT43"))));
    }

    @Test
    void testVerticesIdeais() {
        System.out.println("Testing VerticesIdeais...");

        Map<Location, Criteria> testMap = service.getVerticesIdeais();

        assertEquals(323, testMap.size());

        assertEquals(30,
                testMap.get(new Location("CT1")).getPaths().get(testMap.get(new Location("CT1")).getPaths().size() - 1)
                        .size());

        assertEquals(486165,
                testMap.get(new Location("CT2")).getDistances()
                        .get(testMap.get(new Location("CT1")).getDistances().size() - 1));

        List<String> expected = Arrays.asList("CT1", "CT256", "CT301", "CT262", "CT208", "CT76", "CT166", "CT90",
                "CT288", "CT160", "CT32", "CT3", "CT286", "CT6", "CT104", "CT196", "CT252", "CT38",
                "CT195", "CT129", "CT101", "CT285", "CT110", "CT136", "CT60", "CT117", "CT30", "CT276", "CT34",
                "CT162");
        for (Location location : testMap.get(new Location("CT1")).getPaths()
                .get(testMap.get(new Location("CT1")).getPaths().size() - 1)) {
            assertTrue(expected.contains(location.getId()));
        }
    }
    @Test
    void testGetMinimalPathsPathExistsBetweenLocalizations() {
        System.out.println("Testing GetMinimalPathsPathExistsBetweenLocalizations...");

        Map<Location, Map<Location, Integer>> testMap = service.getMinimalPaths();
        Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(BASKET_DISTRIBUTION);

        for (Location location1 : testMap.keySet()) {
            location1 = service.locationById(location1.getId());
            for (Location location2 : testMap.get(location1).keySet()) {
                location2 = service.locationById(location2.getId());
                assertTrue(minDistGraph.edge(location1, location2) != null);
            }
        }
    }

    @Test
    void testGetMinimalPathsDistanceBetweenLocalizations() {
        System.out.println("Testing GetMinimalPathsDistanceBetweenLocalizations...");

        Map<Location, Map<Location, Integer>> testMap = service.getMinimalPaths();
        Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(BASKET_DISTRIBUTION);

        for (Location location1 : testMap.keySet()) {
            location1 = service.locationById(location1.getId());
            for (Location location2 : testMap.get(location1).keySet()) {
                location2 = service.locationById(location2.getId());
                //verificar se a distancia entre dois locais está correta
                assertEquals(minDistGraph.edge(location1, location2).getWeight(), testMap.get(location1).get(location2));
            }
        }
    }

    @Test
    void testGetMinimalPathsMinimalDistanceBetweenLocalizations() {
        System.out.println("Testing GetMinimalPathsMinimalDistanceBetweenLocalizations...");

        Map<Location, Map<Location, Integer>> testMap = service.getMinimalPaths();
        Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(BASKET_DISTRIBUTION);

        for (Location location1 : testMap.keySet()) {
            location1 = service.locationById(location1.getId());
            for (Location location2 : testMap.get(location1).keySet()) {
                location2 = service.locationById(location2.getId());
                assertTrue(minDistGraph.edge(location1, location2).getWeight() <= testMap.get(location1).
                        get(location2));
            }
        }
    }

    @Test
    void testGetMinimalPathsDistanceBetweenLocalizationsNotNegative() {
        System.out.println("Testing GetMinimalPathsDistanceBetweenLocalizationsNotNegative...");

        Map<Location, Map<Location, Integer>> testMap = service.getMinimalPaths();

        for (Location location1 : testMap.keySet()) {
            location1 = service.locationById(location1.getId());
            for (Location location2 : testMap.get(location1).keySet()) {
                location2 = service.locationById(location2.getId());
                assertTrue(testMap.get(location1).get(location2) >= 0);
            }
        }
    }

    @Test
    void testGetMinimalPathsPrint() {
        System.out.println("Testing GetMinimalPathsPrint...");

        Map<Location, Map<Location, Integer>> testMap = service.getMinimalPaths();
        int totalDistance = 0;

        for (Location location1 : testMap.keySet()) {
            location1 = service.locationById(location1.getId());
            for (Location location2 : testMap.get(location1).keySet()) {
                location2 = service.locationById(location2.getId());
                System.out.println(location1.getId() + " -> " + location2.getId() + "; Distância: "
                        + testMap.get(location1).get(location2));

                totalDistance += testMap.get(location1).get(location2);
            }
        }
        System.out.println("Distância total: " + totalDistance);
    }

    @Test
    void testGetClustersWith2Hubs(){
        System.out.println("Testing testGetClustersWith2Hubs...");

        List<String> ids = new ArrayList<>();
        ids.add("CT76");
        ids.add("CT161");
        List<Graph<Location, Integer>> newList = graphController.divideIntoClusters(ids);
        assertEquals(221, newList.get(0).numVertices());
        assertEquals(102, newList.get(1).numVertices());
        String locationid = "CT76";
        for (Location location : graphRepository.getBasketDistribution().vertices()) {
            if (locationid.equals(location.getId())) {
                assertTrue(newList.get(0).validVertex(location));
                break;
            }
        }
        locationid = "CT123";
        for (Location location : graphRepository.getBasketDistribution().vertices()) {
            if (locationid.equals(location.getId())) {
                assertTrue(newList.get(0).validVertex(location));
                break;
            }
        }
        locationid = "CT161";
        for (Location location : graphRepository.getBasketDistribution().vertices()) {
            if (locationid.equals(location.getId())) {
                assertTrue(newList.get(1).validVertex(location));
                break;
            }
        }
        locationid = "CT309";
        for (Location location : graphRepository.getBasketDistribution().vertices()) {
            if (locationid.equals(location.getId())) {
                assertTrue(newList.get(1).validVertex(location));
                break;
            }
        }
    }

    @Test
    void testGetClustersWith0Hubs() {
        System.out.println("Testing testGetClustersWith0Hubs...");
        List<String> ids = new ArrayList<>();
        List<Graph<Location, Integer>> newList = graphController.divideIntoClusters(ids);
        assertNull(newList);
    }

    /* Como a complexidade de getSC é muito elevada, este teste usa os small csvs e é só para demonstração
    @Test
    void testGetSC(){
        System.out.println("Testing testGetSC...");
        List<String> ids = new ArrayList<>();
        ids.add("CT10");
        ids.add("CT17");
        List<Graph<Location, Integer>> newList = graphController.divideIntoClusters(ids);
        assertEquals(-0.549414873123169, service.getCoefSil(newList));
    }
     */

}