package isep.lapr3.g094.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.File;

import isep.lapr3.g094.domain.Pair;
import isep.lapr3.g094.domain.type.Criteria;
import isep.lapr3.g094.domain.type.FurthestPoints;
import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.repository.GraphRepository;
import isep.lapr3.g094.repository.Repositories;
import isep.lapr3.g094.struct.graph.Algorithms;
import isep.lapr3.g094.struct.graph.Graph;
import isep.lapr3.g094.struct.graph.Edge;
import isep.lapr3.g094.struct.graph.map.MapGraph;

public class Service {

    private GraphRepository graphRepository;

    public Service() {
        getGraphRepository();
    }

    private GraphRepository getGraphRepository() {
        if (graphRepository == null) {
            Repositories repositories = Repositories.getInstance();

            graphRepository = repositories.getGraphRepository();
        }
        return graphRepository;
    }

    public boolean createLocation(List<String> locations, boolean bigGraph) {
        boolean allAdded = true;
        for (String location : locations) {
            String[] line = location.split(",");
            if (!graphRepository.addLocation(new Location(line[0], Double.parseDouble(line[1].replace(',', '.')),
                    Double.parseDouble(line[2].replace(',', '.')), Integer.parseInt(line[0].substring(2)), null, null),
                    bigGraph)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    public boolean addDistance(List<String> distances, boolean bigGraph) {
        boolean allAdded = true;
        for (String distance : distances) {
            String[] line = distance.split(",");
            if (!graphRepository.addDistance(graphRepository.locationById(line[0], bigGraph),
                    graphRepository.locationById(line[1], bigGraph),
                    Integer.parseInt(line[2]), bigGraph))
                allAdded = false;
        }
        return allAdded;

    }

    public boolean createOpeningHours(List<String> horarios, boolean bigGraph) throws Exception {
        boolean allAdded = true;
        for (String horario : horarios) {
            String[] line = horario.split(",");
            Location location = graphRepository.locationById(line[0], bigGraph);
            if (location != null && location.isHub() == true){
                LocalTime startHour = LocalTime.parse(line[1]);
                LocalTime endHour = LocalTime.parse(line[2]);
                location.setStarHour(startHour);
                location.setEndHour(endHour);
                System.out.println("Horário da Localização com ID " + line[0] + " adicionada com sucesso");
            } else {
                allAdded = false;
                System.err.println("Horário da Localização com ID " + line[0] + " não existe ou não é um hub");
            }
        }
        return allAdded;
    }

    public int keyLocation(Location location, boolean bigGraph) {
        return graphRepository.keyLocation(location, bigGraph);
    }

    public Location locationByKey(int key, boolean bigGraph) {
        return graphRepository.locationByKey(key, bigGraph);
    }

    public Location locationById(String id, boolean bigGraph) {
        return graphRepository.locationById(id, bigGraph);
    }

    public Integer distanceLocations(String id1, String id2, boolean bigGraph) {
        Location location1 = graphRepository.locationById(id1, bigGraph);
        Location location2 = graphRepository.locationById(id2, bigGraph);
        return graphRepository.distanceLocations(location1, location2, bigGraph);
    }

    public int getNumLocations(boolean bigGraph) {
        return graphRepository.getNumLocations(bigGraph);
    }

    public int getNumDistances(boolean bigGraph) {
        return graphRepository.getNumDistances(bigGraph);
    }

    public MapGraph<Location, Integer> getGraph(boolean bigGraph) {
        return graphRepository.getGraph(bigGraph);
    }

    public Map<Location, Criteria> getVerticesIdeais(int order, int numberOfHubs, boolean bigGraph) {
        Map<Location, Criteria> map = new HashMap<>();
        int numberMinimumPaths = 0;
        int i;
        for (Location location : getGraph(bigGraph).vertices()) {
            int degree = getGraph(bigGraph).inDegree(location);
            ArrayList<LinkedList<Location>> locationPaths = new ArrayList<>();
            ArrayList<Integer> locationDistance = new ArrayList<>();
            Algorithms.shortestPaths(getGraph(bigGraph), location, Integer::compare, Integer::sum,
                    0, locationPaths, locationDistance);

            int totalDistance = locationDistance.stream().mapToInt(Integer::intValue).sum();

            List<Integer> indices = IntStream.range(0, locationDistance.size()).mapToObj(Integer::valueOf)
                    .collect(Collectors.toList());
            indices.sort(Comparator.comparing(locationDistance::get));
            List<LinkedList<Location>> sortedLocationPaths = indices.stream()
                    .map(locationPaths::get)
                    .collect(Collectors.toList());
            locationPaths.clear();
            locationPaths.addAll(sortedLocationPaths);
            locationPaths.remove(0);
            Criteria criteria = new Criteria(degree, locationPaths, numberMinimumPaths, totalDistance);
            map.put(location, criteria);
        }
        for (Map.Entry<Location, Criteria> entry : map.entrySet()) {
            numberMinimumPaths = 0;
            Location location = entry.getKey();
            for (Map.Entry<Location, Criteria> entry2 : map.entrySet()) {
                if (!entry.getKey().equals(entry2.getKey())) {
                    for (i = 0; i < entry2.getValue().getPaths().size(); i++) {
                        if (entry2.getValue().getPaths().get(i).contains(location)) {
                            numberMinimumPaths++;
                        }
                    }
                }
            }
            entry.getValue().setNumberMinimumPaths(numberMinimumPaths);
        }
        map = sortByValue(order, map);
        int numberHubs = numberOfHubs;
        setHubs(0, numberHubs, map);
        return map;
    }

    private void setHubs(int i, int numberHubs, Map<Location, Criteria> map) {
        if (numberHubs <= 0) {
            return;
        }
        if (i < map.size()) {
            Location location = (Location) map.keySet().toArray()[i];
            location.setHub(true);
            setHubs(i + 1, numberHubs - 1, map);
        } else {
            return;
        }
    }

    private static Map<Location, Criteria> sortByValue(int order, Map<Location, Criteria> map) {
        List<Map.Entry<Location, Criteria>> list = new ArrayList<>(map.entrySet());

        if (order == 0)
            list.sort(Comparator.comparing((Map.Entry<Location, Criteria> entry) -> entry.getValue().getDegree())
                .thenComparing(entry -> entry.getValue().getNumberMinimumPaths()).reversed());
        else{
            list.sort(Comparator.comparing((Map.Entry<Location, Criteria> entry) -> entry.getValue().getDistances()).reversed());        
        }
        Map<Location, Criteria> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Location, Criteria> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public Pair<FurthestPoints, Pair<List<Location>, Integer>> getMinimal(int autonomy, boolean bigGraph) {
        // this graph takes a long time to run
        Graph<Location, Integer> graph = getGraph(bigGraph);

        // furthest points
        Pair<Integer, LinkedList<Location>> furthestPoints = furthestPoints(graph);
        // create shortest path
        LinkedList<Location> shortPath = furthestPoints.getSecond();

        int distance = furthestPoints.getFirst();
        int distanceAutonomy = autonomy;
        List<Location> rechargeLocations = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();
        rechargeLocations.add(shortPath.getFirst());
        // get recharge locations
        for (int i = 0; i < shortPath.size() - 1; i++) {
            Location location1 = shortPath.get(i);
            Location location2 = shortPath.get(i + 1);
            int distanceBetweenPoints = graph.edge(location1, location2).getWeight();
            distances.add(distanceBetweenPoints);
            try {
                // if distance between points is greater than autonomy
                if (distanceBetweenPoints > autonomy) {
                    throw new RuntimeException("\nO veiculo ficou sem bateria na viagem entre local: "
                            + location1.getId() + " e " + location2.getId() + "\n");
                }

                distanceAutonomy -= distanceBetweenPoints;
                // if distance autonomy is greater than autonomy
                if (distanceAutonomy < distanceBetweenPoints && rechargeLocations.get(0) != location1) {
                    rechargeLocations.add(location1);
                    distanceAutonomy = autonomy;
                }
            } catch (RuntimeException e) {
                // Declaring ANSI_RESET so that we can reset the color
                System.err.println(e.getMessage());
                break;
            }
        }

        Pair<FurthestPoints, Pair<List<Location>, Integer>> output = new Pair<>(
                new FurthestPoints(new Pair<Location, Location>(shortPath.getFirst(), shortPath.getLast()), shortPath,
                        distances),
                new Pair<>(rechargeLocations, distance));
        return output;
    }

    private Pair<Integer, LinkedList<Location>> furthestPoints(Graph<Location, Integer> g) {

        Integer maxDist = 0;
        Pair<Integer, LinkedList<Location>> furthestPoints = null;

        for (Location location : g.vertices()) {
            ArrayList<LinkedList<Location>> locationPaths = new ArrayList<>();
            ArrayList<Integer> locationDistance = new ArrayList<>();
            Algorithms.shortestPaths(g, location, Integer::compare, Integer::sum, 0, locationPaths,
                    locationDistance);

            for (int i = 0; i < locationDistance.size(); i++) {

                if (locationDistance.get(i) > maxDist) {
                    maxDist = locationDistance.get(i);
                    furthestPoints = new Pair<>(maxDist, locationPaths.get(i));
                }
            }

        }
        return furthestPoints;
    }

    public Map<Location, Map<Location, Integer>> getMinimalPaths(boolean bigGraph) {
        Map<Location, Map<Location, Integer>> map = new HashMap<>();
        MapGraph<Location, Integer> graph = getGraph(bigGraph);
        Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(graph);
        for (Edge<Location, Integer> edge : minDistGraph.edges()) {
            Location location = edge.getVDest();
            Location location1 = edge.getVOrig();
            String locationId = location.getId();
            String location1Id = location1.getId();
            int distance = edge.getWeight();
            if (locationId.compareTo(location1Id) < 0) {
                if (map.containsKey(location)) {
                    map.get(location).put(location1, distance);
                } else {
                    Map<Location, Integer> map1 = new HashMap<>();
                    map1.put(location1, distance);
                    map.put(location, map1);
                }
            } else {
                if (map.containsKey(location1)) {
                    map.get(location1).put(location, distance);
                } else {
                    Map<Location, Integer> map1 = new HashMap<>();
                    map1.put(location, distance);
                    map.put(location1, map1);
                }
            }
        }
        return map;
    }

    public List<Graph<Location, Integer>> divideIntoClusters(List<String> idsSelected, boolean bigGraph) {

        if (!idsSelected.isEmpty()) {
            Set<Location> listHubs = new LinkedHashSet<>();
            for (String id : idsSelected) {
                for (Location location : getGraph(bigGraph).vertices()) {
                    if (id.equals(location.getId())) {
                        listHubs.add(location);
                        break;
                    }
                }
            }
            Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(graphRepository.getGraph(bigGraph));
            LinkedList<Location> shortPath = new LinkedList<>();
            return Algorithms.divideGraph(minDistGraph, listHubs, Integer::compare, Integer::sum, 0, shortPath);
        } else {
            return null;
        }
    }

    public float getCoefSil(List<Graph<Location, Integer>> clusters, boolean bigGraph) {
        LinkedList<Location> shortPath = new LinkedList<>();
        Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(graphRepository.getGraph(bigGraph));
        return Algorithms.getSC(clusters, Integer::compare, Integer::sum, 0, shortPath, minDistGraph);
    }

    public Map<Location, Map<Location, Integer>> getClusters(boolean bigGraph, Set<Location> hubList, int numClusters){
        Map<Location, Map<Location, Integer>> map = new HashMap<>();
        MapGraph<Location, Integer> graph = getGraph(bigGraph);
        Graph<Location, Integer> clusters = Algorithms.divideGraphN(graph, hubList, Integer::compare, Integer::sum, 0, numClusters);
        for (Edge<Location, Integer> edge : clusters.edges()) {
            Location location = edge.getVDest();
            Location location1 = edge.getVOrig();
            String locationId = location.getId();
            String location1Id = location1.getId();
            int distance = edge.getWeight();
            if (locationId.compareTo(location1Id) < 0) {
                if (map.containsKey(location)) {
                    map.get(location).put(location1, distance);
                } else {
                    Map<Location, Integer> map1 = new HashMap<>();
                    map1.put(location1, distance);
                    map.put(location, map1);
                }
            } else {
                if (map.containsKey(location1)) {
                    map.get(location1).put(location, distance);
                } else {
                    Map<Location, Integer> map1 = new HashMap<>();
                    map1.put(location, distance);
                    map.put(location1, map1);
                }
            }
        }
        return map;
    }

    public Map<List<Pair<Location, Integer>>, Integer> allPathsWithLimit(String vOrigin, String vDest,
            int autonomy,
            int velocity, boolean bigGraph) {
        Graph<Location, Integer> graph = getGraph(bigGraph);

        if (!graph.validVertex(new Location(vOrigin))
                || !graph.validVertex(new Location(vDest))) {
            return null;
        }

        List<List<Location>> paths = Algorithms.allPathsWithLimit(graph,
                graphRepository.locationById(vOrigin, bigGraph),
                graphRepository.locationById(vDest, bigGraph), autonomy, Integer::compare,
                Integer::sum, 0);
        Map<List<Pair<Location, Integer>>, Integer> output = new HashMap<>();

        paths.forEach(path -> {
            List<Pair<Location, Integer>> pathWithDistance = new ArrayList<>();
            int pathDistance = 0;
            for (int i = 0; i < path.size() - 1; i++) {
                Location loc1 = path.get(i);
                Location loc2 = path.get(i + 1);
                Integer distance = graph.edge(loc1, loc2).getWeight();
                pathDistance += distance;
                pathWithDistance.add(new Pair<>(loc1, distance));
            }
            pathWithDistance.add(new Pair<>(path.getLast(), 0));
            output.put(pathWithDistance, pathDistance);

        });
        // order a map by value
        return output.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public boolean generateInitialDataCSV(boolean bigGraph) {
        MapGraph<Location, Integer> graph = graphRepository.getGraph(bigGraph);
        List<String> data = transformData(graph);
        String fileName;
        if (bigGraph)
            fileName = "BigGraph.csv";
        else
            fileName = "SmallGraph.csv";

        return makeCsvFile(fileName, data);
    }

    public boolean generateDataCSV(MapGraph<Location, Integer> graph) {
        List<String> data = transformData(graph);
        int numFiles = new java.io.File("src/main/resources/esinf/output/").listFiles().length;
        String fileName = "Graph" + numFiles + ".csv";
        return makeCsvFile(fileName, data);
    }

    private List<String> transformData(MapGraph<Location, Integer> graph) {
        List<String> data = new ArrayList<>();
        Set<String> addedEdges = new HashSet<>();
        for (Edge<Location, Integer> edge : graph.edges()) {
            if (edge.getVOrig() == null || edge.getVDest() == null) {
                continue;
            }
            if (edge.getVOrig().getId().equals(edge.getVDest().getId())) {
                continue;
            }

            String edgeStr = edge.getVOrig().getId() + "-" + edge.getVDest().getId();
            String oppositeEdgeStr = edge.getVDest().getId() + "-" + edge.getVOrig().getId();

            if (addedEdges.contains(oppositeEdgeStr)) {
                continue;
            }

            addedEdges.add(edgeStr);

            data.add(edge.getVOrig().getId() + "," + edge.getVDest().getId() + "," + edge.getWeight());
        }
        return data;
    }

    private static boolean makeCsvFile(String fileName, List<String> data) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("src/main/resources/esinf/output/" + fileName, false))) {
            // Write the header line
            writer.write("source,target,value");
            writer.newLine();

            for (String item : data) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean convertMapToMapGraph(Map<Location, Map<Location, Integer>> map, MapGraph<Location, Integer> graph) {
        for (Map.Entry<Location, Map<Location, Integer>> entry : map.entrySet()) {
            Location origin = entry.getKey();
            for (Map.Entry<Location, Integer> entry2 : entry.getValue().entrySet()) {
                Location destination = entry2.getKey();
                int distance = entry2.getValue();
                graph.addEdge(origin, destination, distance);
            }
        }
        Set<Location> uniqueLocations = new HashSet<>();
        for (Map.Entry<Location, Map<Location, Integer>> entry : map.entrySet()) {
            uniqueLocations.add(entry.getKey());
            uniqueLocations.addAll(entry.getValue().keySet());
        }

        if (graph.numVertices() == uniqueLocations.size()) {
            return true;
        } else {
            return false;
        }
    }

    public String getLatestFileFromDirectory(String dir) {
        URL url = getClass().getClassLoader().getResource(dir);
        if (url == null) {
            System.out.println("Directory not found: " + dir);
            return "";
        }

        File folder = new File(url.getPath());
        File[] listOfFiles = folder.listFiles();
        String latestFile = "";
        long lastModified = Long.MIN_VALUE;

        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.lastModified() > lastModified) {
                    latestFile = file.getName();
                    lastModified = file.lastModified();
                }
            }
        }

        return latestFile;
    }

    public void maximizedPath(String idOrigem, LocalTime time, double autonomy, int velocity, Boolean bigGraph) {
        Location curLocation = graphRepository.locationById(idOrigem, bigGraph);
        Graph<Location, Integer> graph = getGraph(bigGraph); 
        ArrayList<LinkedList<Pair<Location, Double>>> paths = new ArrayList<>();
        for (Location location : graph.vertices()) {
            Algorithms.allPathsWithAutonomyMax(graph, curLocation, location, autonomy, paths);                
        }
    }

    private Duration unloadingTime() {
        return Duration.ofMinutes(new Random().nextInt(10) + 1);
    }

    private Duration chargingTime(int autonomy, int leftAutonomy) {
        double gainedAutonomyPerMinute = 1016.6666666667;
        return Duration.ofMinutes(Math.round((autonomy - leftAutonomy) / gainedAutonomyPerMinute));
    }

    private Duration travelTime(int distance, int velocity) {
        return Duration.ofMinutes((long) (60 * ((double) distance / velocity)));
    }

    public boolean idExists(String idOrigem, Boolean bigGraph) {
        Location curLocation = graphRepository.locationById(idOrigem, bigGraph);
        if (curLocation == null) {
            System.err.println("Localização não existe");
            return false;
        }
        return true;
    }

    public MapGraph<Location, Integer> filterGraph(MapGraph<Location, Integer> originalGraph) {
        MapGraph<Location, Integer> filteredGraph = new MapGraph<>(false);
    
        for (Location location : originalGraph.vertices()) {
            if (location.isHub()) {
                filteredGraph.addVertex(location);
            }
        }
    
        for (Location location : filteredGraph.vertices()) {
            for (Location adjLocation : originalGraph.adjVertices(location)) {
                if (filteredGraph.validVertex(adjLocation)) {
                    filteredGraph.addEdge(location, adjLocation, originalGraph.edge(location, adjLocation).getWeight());
                }
            }
        }
    
        return filteredGraph;
    }

    public Pair<Integer, List<Location>> maximumCapacity(MapGraph<Location, Integer> graph, Location origin, Location destination) {
        Pair<Integer, List<Location>> result = Algorithms.fordFulkerson(graph, origin, destination);
        Integer dividedFlow = result.getFirst() / 10000;
        return new Pair<>(dividedFlow, result.getSecond());
    }
}