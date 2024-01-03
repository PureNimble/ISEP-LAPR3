package isep.lapr3.g094.struct;

import isep.lapr3.g094.struct.graph.Edge;
import isep.lapr3.g094.struct.graph.Graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import isep.lapr3.g094.application.controller.GraphController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.type.Criteria;
import isep.lapr3.g094.domain.type.FurthestPoints;
import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.repository.GraphRepository;
import isep.lapr3.g094.repository.Repositories;
import isep.lapr3.g094.services.Service;
import isep.lapr3.g094.services.Services;
import isep.lapr3.g094.struct.graph.Algorithms;
import isep.lapr3.g094.struct.graph.map.MapGraph;
import isep.lapr3.g094.domain.Pair;

public class MainTest {

    private GraphController graphController;
    private GraphRepository graphRepository;
    private ImportController importController;
    private Service service;
    private MapGraph<Location, Integer> BASKET_DISTRIBUTION = new MapGraph<>(false);
    private MapGraph<Location, Integer> SMALL_BASKET_DISTRIBUTION = new MapGraph<>(false);

    @BeforeEach
    void setUp() {
        Repositories repositories = Repositories.getInstance();
        Services services = Services.getInstance();
        graphRepository = repositories.getGraphRepository();
        service = services.getService();
        importController = new ImportController();
        importController.importToGraph(true);
        importController.importToGraph(false);
        graphController = new GraphController();
        BASKET_DISTRIBUTION = graphRepository.getGraph(true);
        SMALL_BASKET_DISTRIBUTION = graphRepository.getGraph(false);
    }

    @Test
    void testBasketDistributionLocationsNum() {
        System.out.println("Testing BasketDistributionLocationsNum...");

        assertEquals(323, graphController.getNumLocations(true));
    }

    @Test
    void testBasketDistributionDistancesNum() {
        System.out.println("Testing BasketDistributionDistancesNum...");

        assertEquals(1566, graphController.getNumDistances(true));
    }

    @Test
    void testBasketDistributionNullLocation() {
        System.out.println("Testing BasketDistributionNullLocation...");

        assertThrows(RuntimeException.class, () -> graphRepository.addLocation(null, true));
        assertThrows(RuntimeException.class, () -> graphRepository.addDistance(null, null, 0, true));
    }

    @Test
    void testBasketDistributionLocationByKey() {
        System.out.println("Testing BasketDistributionLocationByKey...");

        assertNull(service.locationByKey(1000000, true));
        assertEquals("CT43", service.locationByKey(0, true).getId());
        assertEquals("CT211", service.locationByKey(69, true).getId());
        assertEquals("CT305", service.locationByKey(123, true).getId());
        assertEquals("CT253", service.locationByKey(265, true).getId());
        assertEquals("CT46", service.locationByKey(321, true).getId());
        assertEquals("CT159", service.locationByKey(322, true).getId());
        assertNull(service.locationByKey(999, true));
    }

    @Test
    void testBasketDistributionLocationById() {
        System.out.println("Testing BasketDistributionLocationById...");

        assertNull(service.locationById("CT999", true));
        assertEquals(new Location("CT43"), service.locationById("CT43", true));
        assertEquals(new Location("CT211"), service.locationById("CT211", true));
        assertEquals(new Location("CT305"), service.locationById("CT305", true));
        assertEquals(new Location("CT253"), service.locationById("CT253", true));
        assertEquals(new Location("CT46"), service.locationById("CT46", true));
        assertEquals(new Location("CT159"), service.locationById("CT159", true));
    }

    @Test
    void testBasketDistributionKeyLocation() {
        System.out.println("Testing BasketDistributionKeyLocation...");

        assertEquals(-1, service.keyLocation(new Location("CT999"), true));
        assertEquals(0, service.keyLocation(new Location("CT43"), true));
        assertEquals(69, service.keyLocation(new Location("CT211"), true));
        assertEquals(123, service.keyLocation(new Location("CT305"), true));
        assertEquals(265, service.keyLocation(new Location("CT253"), true));
        assertEquals(321, service.keyLocation(new Location("CT46"), true));
        assertEquals(322, service.keyLocation(new Location("CT159"), true));
    }

    @Test
    void testBasketDistributionLocationByID() {
        System.out.println("Testing BasketDistributionLocationByID...");

        Location location1 = new Location("CT123", 0, 0, 0, null, null);
        Location location2 = new Location("CT268", 0, 0, 0, null, null);
        graphRepository.addLocation(location1, true);
        graphRepository.addLocation(location2, true);

        assertEquals(location1, graphController.locationById("CT123", true));
        assertEquals(location2, graphController.locationById("CT268", true));
        assertNull(graphController.locationById("CT999", true));
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

        ArrayList<LinkedList<Location>> paths = Algorithms.allPaths(testGraph, new Location("CT1"),
                new Location("CT5"));
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

        Map<Location, Criteria> testMap = service.getVerticesIdeais(1, 5, true);

        assertEquals(323, testMap.size());

        assertEquals(30,
                testMap.get(new Location("CT1")).getPaths().get(testMap.get(new Location("CT1")).getPaths().size() - 1)
                        .size());

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

        Map<Location, Map<Location, Integer>> testMap = service.getMinimalPaths(true);
        Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(BASKET_DISTRIBUTION);

        for (Location location1 : testMap.keySet()) {
            location1 = service.locationById(location1.getId(), true);
            for (Location location2 : testMap.get(location1).keySet()) {
                location2 = service.locationById(location2.getId(), true);
                assertTrue(minDistGraph.edge(location1, location2) != null);
            }
        }
    }

    @Test
    void testGetMinimalPathsDistanceBetweenLocalizations() {
        System.out.println("Testing GetMinimalPathsDistanceBetweenLocalizations...");

        Map<Location, Map<Location, Integer>> testMap = service.getMinimalPaths(true);
        Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(BASKET_DISTRIBUTION);

        for (Location location1 : testMap.keySet()) {
            location1 = service.locationById(location1.getId(), true);
            for (Location location2 : testMap.get(location1).keySet()) {
                location2 = service.locationById(location2.getId(), true);
                // verificar se a distancia entre dois locais está correta
                assertEquals(minDistGraph.edge(location1, location2).getWeight(),
                        testMap.get(location1).get(location2));
            }
        }
    }

    @Test
    void testGetMinimalPathsMinimalDistanceBetweenLocalizations() {
        System.out.println("Testing GetMinimalPathsMinimalDistanceBetweenLocalizations...");

        Map<Location, Map<Location, Integer>> testMap = service.getMinimalPaths(true);
        Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(BASKET_DISTRIBUTION);

        for (Location location1 : testMap.keySet()) {
            location1 = service.locationById(location1.getId(), true);
            for (Location location2 : testMap.get(location1).keySet()) {
                location2 = service.locationById(location2.getId(), true);
                assertTrue(
                        minDistGraph.edge(location1, location2).getWeight() <= testMap.get(location1).get(location2));
            }
        }
    }

    @Test
    void testGetMinimalPathsDistanceBetweenLocalizationsNotNegative() {
        System.out.println("Testing GetMinimalPathsDistanceBetweenLocalizationsNotNegative...");

        Map<Location, Map<Location, Integer>> testMap = service.getMinimalPaths(true);

        for (Location location1 : testMap.keySet()) {
            location1 = service.locationById(location1.getId(), true);
            for (Location location2 : testMap.get(location1).keySet()) {
                location2 = service.locationById(location2.getId(), true);
                assertTrue(testMap.get(location1).get(location2) >= 0);
            }
        }
    }

    @Test
    void testGetMinimalPathsPrint() {
        System.out.println("Testing GetMinimalPathsPrint...");

        Map<Location, Map<Location, Integer>> testMap = service.getMinimalPaths(true);
        int totalDistance = 0;

        for (Location location1 : testMap.keySet()) {
            location1 = service.locationById(location1.getId(), true);
            for (Location location2 : testMap.get(location1).keySet()) {
                location2 = service.locationById(location2.getId(), true);
                System.out.println(location1.getId() + " -> " + location2.getId() + "; Distância: "
                        + testMap.get(location1).get(location2));

                totalDistance += testMap.get(location1).get(location2);
            }
        }
        System.out.println("Distância total: " + totalDistance);
    }

    //@Test
    void testGetClustersWith2Hubs() {
        System.out.println("Testing GetClustersWith2Hubs...");

        List<String> ids = new ArrayList<>();
        ids.add("CT76");
        ids.add("CT161");
        List<Graph<Location, Integer>> newList = graphController.divideIntoClusters(ids, true);
        assertEquals(221, newList.get(0).numVertices());
        assertEquals(102, newList.get(1).numVertices());
        String locationid = "CT76";
        for (Location location : graphRepository.getGraph(true).vertices()) {
            if (locationid.equals(location.getId())) {
                assertTrue(newList.get(0).validVertex(location));
                break;
            }
        }
        locationid = "CT123";
        for (Location location : graphRepository.getGraph(true).vertices()) {
            if (locationid.equals(location.getId())) {
                assertTrue(newList.get(0).validVertex(location));
                break;
            }
        }
        locationid = "CT161";
        for (Location location : graphRepository.getGraph(true).vertices()) {
            if (locationid.equals(location.getId())) {
                assertTrue(newList.get(1).validVertex(location));
                break;
            }
        }
        locationid = "CT309";
        for (Location location : graphRepository.getGraph(true).vertices()) {
            if (locationid.equals(location.getId())) {
                assertTrue(newList.get(1).validVertex(location));
                break;
            }
        }
    }

    @Test
    void testGetClustersWith0Hubs() {
        System.out.println("Testing GetClustersWith0Hubs...");
        List<String> ids = new ArrayList<>();
        List<Graph<Location, Integer>> newList = graphController.divideIntoClusters(ids, true);
        assertNull(newList);
    }

    @Test
    void testDeliveryCircuitPathHubsNotNegative(){
        System.out.println("Testing DeliveryCircuitPathHubsNotNegative...");
        int nHubs = -1;
        List<Location> path = new ArrayList<>();
        path=service.deliveryCircuitPath("CT1", nHubs, true);
        assertNull(path);
    }

    @Test
    void testDeliveryCircuitPathIdDoesNotExist(){
        System.out.println("Testing DeliveryCircuitPathIdDoesNotExist...");
        int nHubs = 6;
        List<Location> path = new ArrayList<>();
        path=service.deliveryCircuitPath("NaoExiste", nHubs, true);
        assertNull(path);
    }

    @Test
    void testDeliveryCircuitPathHubsCanNotBeGreaterThan7(){
        System.out.println("Testing DeliveryCircuitPathHubsCanNotBeGreaterThan7...");
        int nHubs = 8;
        List<Location> path = new ArrayList<>();
        path=service.deliveryCircuitPath("CT1", nHubs, true);
        assertNull(path);
    }

    @Test
    void testDeliveryCircuitPathHubsCanNotBeLessThan5(){
        System.out.println("Testing DeliveryCircuitPathHubsCanNotBeLessThan5...");
        int nHubs = 4;
        List<Location> path = new ArrayList<>();
        path=service.deliveryCircuitPath("CT1", nHubs, true);
        assertNull(path);
    }

    //Verificar que o metodo deliveryCircuitPath retorna a informação correta
    @Test
    void testDeliveryCircuitPath(){
        System.out.println("Testing DeliveryCircuitPath...");
        int nHubs = 5;
        graphController.getVerticesIdeais(1,17,false);
        List<Location> path = new ArrayList<>();
        path=service.deliveryCircuitPath("CT1", nHubs, false);
        assertEquals(5, path.size());
    }

    /*
     * @Test
     * void testGetSC() {
     * System.out.println("Testing GetSC...");
     * List<String> ids = new ArrayList<>();
     * ids.add("CT10");
     * ids.add("CT17");
     * float actual = service.getCoefSilManually(ids,
     * graphRepository.getSmallGraph());
     * assertEquals(-0.549414873123169, actual);
     * }
     */

    @Test
    void testMinimalOptimalCase() {
        System.out.println("Testing MinimalOptimalCase...");

        int autonomy = 250;

        Pair<FurthestPoints, Pair<List<Location>, Integer>> testPair = service.getMinimal(autonomy * 1000, true);

        assertEquals(3, testPair.getSecond().getFirst().size());

        assertEquals(685116, testPair.getSecond().getSecond());
        // big graph
        List<String> graph_output = Arrays.asList("CT162", "CT34", "CT276", "CT30", "CT228", "CT133", "CT113", "CT147",
                "CT210", "CT78", "CT41", "CT255", "CT320", "CT68", "CT72", "CT272", "CT37", "CT205", "CT193", "CT230",
                "CT148", "CT126", "CT222", "CT180", "CT42", "CT107", "CT297", "CT115", "CT83", "CT23", "CT143",
                "CT194");

        for (Location location : testPair.getSecond().getFirst()) {
            assertTrue(graph_output.contains(location.getId()));
        }
        // Origem
        assertEquals("CT162", testPair.getFirst().getPair().getFirst().getId());
        // Destino
        assertEquals("CT194", testPair.getFirst().getPair().getSecond().getId());

        int locationsSize = testPair.getFirst().getLocations().size();
        assertEquals(32, locationsSize);
        // shortestLongestPath
        List<String> locations = Arrays.asList("CT162", "CT34", "CT276", "CT30", "CT228", "CT133", "CT113", "CT147",
                "CT210", "CT78", "CT41", "CT255", "CT320", "CT68", "CT72", "CT272", "CT37", "CT205", "CT193", "CT230",
                "CT148", "CT126", "CT222", "CT180", "CT42", "CT107", "CT297", "CT115", "CT83", "CT23", "CT143",
                "CT194");
        List<String> output = new ArrayList<>();

        for (int i = 0; i < locationsSize; i++) {
            output.add(testPair.getFirst().getLocations().get(i).getId());
        }
        assertArrayEquals(locations.toArray(), output.toArray());
        List<Integer> distanceOutput = new ArrayList<>();
        List<Integer> distances = Arrays.asList(35534, 19597, 11235, 11236, 15117, 12785, 12830, 10235, 16923, 16596,
                27513, 17382, 18500, 13897, 8761, 18341, 22908, 16684, 20487, 25392, 12343, 27495, 40958, 30377, 20849,
                30205, 36210, 43344, 22979, 45292, 23111);
        for (int i = 0; i < locationsSize; i++) {
            if (i < testPair.getFirst().getDistances().size()) {
                distanceOutput.add(testPair.getFirst().getDistances().get(i));
            }
        }
        // shortestLongestPathDistances
        assertArrayEquals(distances.toArray(), distanceOutput.toArray());
        // distancia total
        assertEquals(685116, testPair.getSecond().getSecond());

        // locais de carregamento
        List<String> expected = Arrays.asList("CT162", "CT68", "CT180");
        List<String> locaisDeCarregamento = new ArrayList<>();
        for (int i = 0; i < testPair.getSecond().getFirst().size(); i++) {
            locaisDeCarregamento.add(testPair.getSecond().getFirst().get(i).getId());
        }
        assertArrayEquals(expected.toArray(), locaisDeCarregamento.toArray());

    }

    @Test
    void testMinimalNoFuel() {
        System.out.println("Testing MinimalNoFuel...");
        int autonomy = 10;

        Pair<FurthestPoints, Pair<List<Location>, Integer>> testPair = service.getMinimal(autonomy * 1000, true);

        assertEquals(1, testPair.getSecond().getFirst().size());

        assertEquals(685116, testPair.getSecond().getSecond());
        // big graph
        List<String> small_output = Arrays.asList("CT162", "CT34", "CT276", "CT30", "CT228", "CT133", "CT113", "CT147",
                "CT210", "CT78", "CT41", "CT255", "CT320", "CT68", "CT72", "CT272", "CT37", "CT205", "CT193", "CT230",
                "CT148", "CT126", "CT222", "CT180", "CT42", "CT107", "CT297", "CT115", "CT83", "CT23", "CT143",
                "CT194");

        for (Location location : testPair.getSecond().getFirst()) {
            assertTrue(small_output.contains(location.getId()));
        }
        // Origem
        assertEquals("CT162", testPair.getFirst().getPair().getFirst().getId());
        // Destino
        assertEquals("CT194", testPair.getFirst().getPair().getSecond().getId());

        int locationsSize = testPair.getFirst().getLocations().size();
        assertEquals(32, locationsSize);

        List<Integer> distanceOutput = new ArrayList<>();
        List<Integer> shortestLongestPathDistances = Arrays.asList(35534);
        for (int i = 0; i < locationsSize; i++) {
            if (i < testPair.getFirst().getDistances().size()) {
                distanceOutput.add(testPair.getFirst().getDistances().get(i));
            }
        }
        // shortestLongestPathDistances
        assertArrayEquals(shortestLongestPathDistances.toArray(), distanceOutput.toArray());
        // distancia total
        assertEquals(685116, testPair.getSecond().getSecond());

        // locais de carregamento
        List<String> expected = Arrays.asList("CT162");
        List<String> locaisDeCarregamento = new ArrayList<>();
        for (int i = 0; i < testPair.getSecond().getFirst().size(); i++) {
            locaisDeCarregamento.add(testPair.getSecond().getFirst().get(i).getId());
        }
        assertArrayEquals(expected.toArray(), locaisDeCarregamento.toArray());

    }

    @Test
    void allPathsWithLimitSmallGraph() {

        System.out.println("Testing allPathsWithLimit (smallGraph)...");

        int autonomy = 300;
        String idOrigem = "CT15";
        String idDestino = "CT17";
        int velocity = 120;

        Map<List<Pair<Location, Integer>>, Integer> paths = graphController.allPathsWithLimit(idOrigem,
                idDestino, autonomy * 1000, velocity, false);

        paths.forEach((k, v) -> {
            assertEquals(idOrigem, k.getFirst().getFirst().getId());
            assertEquals(idDestino, k.getLast().getFirst().getId());
        });
        //shortest path
        List<Pair<Location, Integer>> shortestPath = paths.entrySet().iterator().next().getKey();
        Integer shortestDistance = paths.entrySet().iterator().next().getValue();
        List<Pair<Location, Integer>> expectedShortestPath = Arrays.asList(new Pair<>(new Location("CT15"), 43598),
                new Pair<>(new Location("CT3"), 68957), new Pair<>(new Location("CT16"), 79560),
                new Pair<>(new Location("CT17"), 0));
        assertEquals(expectedShortestPath, shortestPath);
        assertEquals(192115, shortestDistance);

    }

    @Test
    void allPathsWithLimitBigGraph() {

        System.out.println("Testing allPathsWithLimit (bigGraph)...");

        int autonomy = 100;
        String idOrigem = "CT137";
        String idDestino = "CT17";
        int velocity = 120;

        Map<List<Pair<Location, Integer>>, Integer> paths = graphController.allPathsWithLimit(idOrigem,
                idDestino, autonomy * 1000, velocity, true);

        paths.forEach((k, v) -> {
            assertEquals(idOrigem, k.getFirst().getFirst().getId());
            assertEquals(idDestino, k.getLast().getFirst().getId());
        });
        //shortest path
        List<Pair<Location, Integer>> shortestPath = paths.entrySet().iterator().next().getKey();
        Integer shortestDistance = paths.entrySet().iterator().next().getValue();
        List<Pair<Location, Integer>> expectedShortestPath = Arrays.asList(new Pair<>(new Location("CT137"), 7921),
                new Pair<>(new Location("CT225"), 34652), new Pair<>(new Location("CT17"), 0));
        assertEquals(expectedShortestPath, shortestPath);
        assertEquals(42573, shortestDistance);

    }

    @Test
    void allPathsWithLimitNoPath() {

        System.out.println("Testing allPathsWithLimit (noPath)...");

        int autonomy = 40;
        String idOrigem = "CT137";
        String idDestino = "CT17";
        int velocity = 120;

        Map<List<Pair<Location, Integer>>, Integer> paths = graphController.allPathsWithLimit(idOrigem,
                idDestino, autonomy * 1000, velocity, true);

        assertTrue(paths.isEmpty());

    }

    @Test
    void allPathsWithLimitInvalidLocation() {
        System.out.println("Testing allPathsWithLimit (invalidLocation)...");

        int autonomy = 40;
        String idOrigem = "CT137";
        String idDestino = "CT999";
        int velocity = 120;

        assertEquals(null, graphController.allPathsWithLimit(idOrigem,
                idDestino, autonomy * 1000, velocity, true));

    }

    @Test
    void testMaximumCapacityNetworkBigGraph() {
        System.out.println("Testing MaximumCapacityNetworkBigGraph...");

        Location idOrigem = new Location("CT56");
        Location idDestino = new Location("CT234");
        Location ct13 = new Location("CT13");
        Location ct245 = new Location("CT245");
        Location ct94 = new Location("CT94");

        service.getVerticesIdeais(1, 250, true);
        MapGraph<Location, Integer> hubGraph = graphController.filterGraph(BASKET_DISTRIBUTION);
        Pair<Integer, MapGraph<Location, Integer>> testPair = graphController.maximumCapacity(hubGraph, idOrigem,
                idDestino);

        assertEquals(Integer.valueOf(2), testPair.getSecond().edge(idOrigem, ct13).getWeight());
        assertEquals(Integer.valueOf(2), testPair.getSecond().edge(ct13, ct245).getWeight());
        assertEquals(Integer.valueOf(2), testPair.getSecond().edge(ct245, ct94).getWeight());
        assertEquals(Integer.valueOf(2), testPair.getSecond().edge(idOrigem, ct94).getWeight());
    }

    @Test
    void testMaximumCapacityNetworkPrincipleBigGraph() {
        System.out.println("Testing MaximumCapacityNetworkPrincipleBigGraph...");

        Location idOrigem = new Location("CT8");
        Location idDestino = new Location("CT47");
        Location idCT296 = new Location("CT296");
        service.getVerticesIdeais(1, 250, true);
        MapGraph<Location, Integer> hubGraph = graphController.filterGraph(BASKET_DISTRIBUTION);
        Pair<Integer, MapGraph<Location, Integer>> testPair = graphController.maximumCapacity(hubGraph, idOrigem,
                idDestino);
        int outFlow = testPair.getSecond().vertices().stream()
            .mapToInt(location -> {
                Edge<Location, Integer> edge = testPair.getSecond().edge(idOrigem, location);
                return edge != null ? (int) edge.getWeight() : 0;
            })
            .sum();

        int inFlow = testPair.getSecond().vertices().stream()
            .mapToInt(location -> {
                Edge<Location, Integer> edge = testPair.getSecond().edge(location, idDestino);
                return edge != null ? (int) edge.getWeight() : 0;
            })
            .sum();
        
        int outFlowCT296 = testPair.getSecond().vertices().stream()
            .mapToInt(location -> {
                Edge<Location, Integer> edge = testPair.getSecond().edge(idCT296, location);
                return edge != null ? (int) edge.getWeight() : 0;
            })
            .sum();

        int inFlowCT296 = testPair.getSecond().vertices().stream()
            .mapToInt(location -> {
                Edge<Location, Integer> edge = testPair.getSecond().edge(location, idCT296);
                return edge != null ? (int) edge.getWeight() : 0;
            })
            .sum();


        assertEquals(testPair.getFirst(), outFlow);
        assertEquals(testPair.getFirst(), inFlow);
        assertEquals(outFlowCT296, inFlowCT296);
    }

    @Test
    void testMaximumCapacityBigGraph() {
        System.out.println("Testing MaximumCapacityBigGraph...");

        Location idOrigem = new Location("CT56");
        Location idDestino = new Location("CT234");
        service.getVerticesIdeais(1, 250, true);
        MapGraph<Location, Integer> hubGraph = graphController.filterGraph(BASKET_DISTRIBUTION);
        Pair<Integer, MapGraph<Location, Integer>> testPair = graphController.maximumCapacity(hubGraph, idOrigem,
                idDestino);

        assertEquals(testPair.getFirst(), 6);
    }

    @Test
    void testMaximumCapacityBigGraphNoFlow() {
        System.out.println("Testing MaximumCapacityBigGraphNoFlow...");

        Location idOrigem = new Location("CT196");
        Location idDestino = new Location("CT300");
        service.getVerticesIdeais(1, 250, true);
        MapGraph<Location, Integer> hubGraph = graphController.filterGraph(BASKET_DISTRIBUTION);
        Pair<Integer, MapGraph<Location, Integer>> testPair = graphController.maximumCapacity(hubGraph, idOrigem,
                idDestino);

        assertEquals(0, testPair.getFirst());
        assertEquals(Collections.emptyList(), testPair.getSecond().vertices());
        assertEquals(Collections.emptyList(), testPair.getSecond().edges());
    }

    @Test
    void testMaximumCapacityNetworkSmallGraph() {
        System.out.println("Testing MaximumCapacityNetworkSmallGraph...");

        Location idOrigem = new Location("CT2");
        Location idDestino = new Location("CT8");
        Location ct7 = new Location("CT7");
        Location ct14 = new Location("CT14");

        service.getVerticesIdeais(1, 8, false);
        MapGraph<Location, Integer> hubGraph = graphController.filterGraph(SMALL_BASKET_DISTRIBUTION);
        Pair<Integer, MapGraph<Location, Integer>> testPair = graphController.maximumCapacity(hubGraph, idOrigem,
                idDestino);

        assertEquals(Integer.valueOf(12), testPair.getSecond().edge(idOrigem, idDestino).getWeight());
        assertEquals(Integer.valueOf(6), testPair.getSecond().edge(idOrigem, ct7).getWeight());
        assertEquals(Integer.valueOf(11), testPair.getSecond().edge(idOrigem, ct14).getWeight());
        assertEquals(Integer.valueOf(17), testPair.getSecond().edge(ct14, idDestino).getWeight());
        assertEquals(Integer.valueOf(6), testPair.getSecond().edge(ct7, ct14).getWeight());
    }

    @Test
    void testMaximumCapacitySmallGraph() {
        System.out.println("Testing MaximumCapacitySmallGraph...");

        Location idOrigem = new Location("CT2");
        Location idDestino = new Location("CT8");
        service.getVerticesIdeais(1, 8, false);
        MapGraph<Location, Integer> hubGraph = graphController.filterGraph(SMALL_BASKET_DISTRIBUTION);
        Pair<Integer, MapGraph<Location, Integer>> testPair = graphController.maximumCapacity(hubGraph, idOrigem,
                idDestino);

        assertEquals(testPair.getFirst(), 29);
    }

    @Test
    void testMaximumCapacityNetworkPrincipleSmallGraph() {
        System.out.println("Testing MaximumCapacityNetworkPrincipleSmallGraph...");

        Location idOrigem = new Location("CT2");
        Location idDestino = new Location("CT8");
        Location idCT14 = new Location("CT14");
        service.getVerticesIdeais(1, 250, false);
        MapGraph<Location, Integer> hubGraph = graphController.filterGraph(SMALL_BASKET_DISTRIBUTION);
        Pair<Integer, MapGraph<Location, Integer>> testPair = graphController.maximumCapacity(hubGraph, idOrigem,
                idDestino);
        int outFlow = testPair.getSecond().vertices().stream()
            .mapToInt(location -> {
                Edge<Location, Integer> edge = testPair.getSecond().edge(idOrigem, location);
                return edge != null ? (int) edge.getWeight() : 0;
            })
            .sum();

        int inFlow = testPair.getSecond().vertices().stream()
            .mapToInt(location -> {
                Edge<Location, Integer> edge = testPair.getSecond().edge(location, idDestino);
                return edge != null ? (int) edge.getWeight() : 0;
            })
            .sum();

        int outFlowCT296 = testPair.getSecond().vertices().stream()
            .mapToInt(location -> {
                Edge<Location, Integer> edge = testPair.getSecond().edge(idCT14, location);
                return edge != null ? (int) edge.getWeight() : 0;
            })
            .sum();

        int inFlowCT296 = testPair.getSecond().vertices().stream()
            .mapToInt(location -> {
                Edge<Location, Integer> edge = testPair.getSecond().edge(location, idCT14);
                return edge != null ? (int) edge.getWeight() : 0;
            })
            .sum();


        assertEquals(testPair.getFirst(), outFlow);
        assertEquals(testPair.getFirst(), inFlow);
        assertEquals(outFlowCT296, inFlowCT296);
    }

    @Test
    void testMaximumCapacitySmallGraphNoFlow() {
        System.out.println("Testing MaximumCapacitySmallGraphNoFlow...");

        Location idOrigem = new Location("CT3");
        Location idDestino = new Location("CT7");
        service.getVerticesIdeais(1, 8, false);
        MapGraph<Location, Integer> hubGraph = graphController.filterGraph(SMALL_BASKET_DISTRIBUTION);
        Pair<Integer, MapGraph<Location, Integer>> testPair = graphController.maximumCapacity(hubGraph, idOrigem,
                idDestino);

        assertEquals(0, testPair.getFirst());
        assertEquals(Collections.emptyList(), testPair.getSecond().vertices());
        assertEquals(Collections.emptyList(), testPair.getSecond().edges());
    }

    @Test
    void testMaximumCapacityNoHubs() {
        System.out.println("Testing MaximumCapacityNoHubs...");

        service.resetHubs(true);
        service.resetHubs(false);
        MapGraph<Location, Integer> bigHubGraph = graphController.filterGraph(BASKET_DISTRIBUTION);
        MapGraph<Location, Integer> smallHubGraph = graphController.filterGraph(SMALL_BASKET_DISTRIBUTION);

        assertTrue(bigHubGraph.vertices().isEmpty());
        assertTrue(smallHubGraph.vertices().isEmpty());
    }

    @Test
    void testDivideGraphNWith3Clusters() {
        System.out.println("Testing DivideGraphN...");

        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais(1, 3, true);
        Set<Location> listHubs = new LinkedHashSet<>();
        int i = 0;
        for(Map.Entry<Location, Criteria> entry : idealVertices.entrySet()){
            listHubs.add(entry.getKey());
            i++;
            if(i >= 3) break;
        }

        Map<Location, LinkedList<Location>> clusters = service.getClusters(true, 3, listHubs);

        assertTrue(clusters.keySet().containsAll(listHubs));

        int[] expected = {316, 4, 3};
        int u = 0;
        for(Map.Entry<Location, LinkedList<Location>> cluster : clusters.entrySet()){
            assertEquals(expected[u], cluster.getValue().size());
            u++;
        }
    }

    @Test
    void testMaximizedPath(){
        System.out.println("Testing MaximizedPath...");

        int autonomy = 150000;
        int velocity = 50;
        String idOrigem = "ct3".toUpperCase();
        LocalTime time = LocalTime.of(10, 0);
        LinkedList<Location> topPath = new LinkedList<>();
        LinkedList<LocalTime> topArriveTimes = new LinkedList<>();
        LinkedList<LocalTime> topDepartTimes = new LinkedList<>();
        LinkedList<LocalTime> topAfterChargeTimes = new LinkedList<>();
        LinkedList<LocalTime> topDescargaTimes = new LinkedList<>();
        graphController.getVerticesIdeais(0, 10, false);
        importController.importOpeningHours("esinf/schedules/horarioFuncionamento.csv", false);
        int topDistance = graphController.maximizedPath(idOrigem, time, autonomy, velocity, false, topPath, topArriveTimes, topDepartTimes, topAfterChargeTimes, topDescargaTimes);
        assertEquals(3, topPath.size());
        assertEquals(3, topArriveTimes.size());
        assertEquals(3, topDepartTimes.size());
        assertEquals(3, topAfterChargeTimes.size());
        assertEquals(3, topDescargaTimes.size());
        assertEquals(3, topPath.size());
        assertEquals(113344, topDistance);

        List<String> expectedPath = Arrays.asList("CT3", "CT12", "CT1");
        for (Location location : topPath) {
            assertTrue(expectedPath.contains(location.getId()));
        }

        List<LocalTime> expectedArriveTimes = Arrays.asList(LocalTime.of(10, 0), LocalTime.of(11, 0));
        for (int i = 0; i < 2 && i < topArriveTimes.size(); i++) {
            assertTrue(expectedArriveTimes.contains(topArriveTimes.get(i)));
        }

        List<LocalTime> expectedDepartTimes = Arrays.asList(LocalTime.of(10, 0));
        for (int i = 0; i < 1 && i < topDepartTimes.size(); i++) {
            assertTrue(expectedDepartTimes.contains(topDepartTimes.get(i)));
        }

        List<LocalTime> expectedAfterChargeTimes = Arrays.asList(null, LocalTime.of(10, 0));
        for (int i = 0; i < 2 && i < topAfterChargeTimes.size(); i++) {
            assertTrue(expectedAfterChargeTimes.contains(topAfterChargeTimes.get(i)));
        }

        if (!topDescargaTimes.isEmpty()) {
            assertNull(topDescargaTimes.get(0));
        }

        assertEquals(topDepartTimes.getLast(), topDescargaTimes.getLast());
    }
}