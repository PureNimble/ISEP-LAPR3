package isep.lapr3.g094.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BinaryOperator;
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
            if (location != null && location.isHub() == true) {
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
        MapGraph<Location, Integer> graph = getGraph(bigGraph);
        for (Location location : graph.vertices()) {
            int degree = graph.inDegree(location);
            ArrayList<LinkedList<Location>> locationPaths = new ArrayList<>();
            ArrayList<Integer> locationDistance = new ArrayList<>();
            Algorithms.shortestPaths(graph, location, Integer::compare, Integer::sum,
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
        resetHubs(bigGraph);
        setHubs(0, numberOfHubs, map);
        return map;
    }

    public void resetHubs(boolean bigGraph) {
        for (Location location : getGraph(bigGraph).getVertices()) {
            location.setHub(false);
        }
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
        else {
            list.sort(Comparator.comparing((Map.Entry<Location, Criteria> entry) -> entry.getValue().getDistances())
                    .reversed());
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

    public Map<Location, LinkedList<Location>> getClusters(boolean bigGraph, int numClusters, Set<Location> hubList) {
        MapGraph<Location, Integer> graph = getGraph(bigGraph).clone();
        if (hubList.size() < numClusters) {
            System.out.println("O número de clusters não pode ser maior que o número de hubs do grafo!");
            return null;
        }
        Map<Location, LinkedList<Location>> clusters = Algorithms.divideGraphN(graph, hubList, Integer::compare,
                Integer::sum, 0, numClusters);

        return clusters;
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
        //File resourceFile = new File(getClass().getClassLoader().getResource("esinf/output/").getFile());
        File resourceFile = new File("./output/");
        int numFiles = resourceFile.listFiles().length;
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

    private boolean makeCsvFile(String fileName, List<String> data) {
        //String resourcePath = getClass().getClassLoader().getResource("esinf/output/").getPath();
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("./output/" + fileName, false))) {
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
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = null;
        try {
            url = new File(dir).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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

    public int maximizedPath(String idOrigem, LocalTime time, int autonomy, int velocity, Boolean bigGraph,
            LinkedList<Location> topPath, LinkedList<LocalTime> topArriveTimes, LinkedList<LocalTime> topDepartTimes,
            LinkedList<LocalTime> topAfterChargeTimes, LinkedList<LocalTime> topDescargaTimes) {
        Location curLocation = graphRepository.locationById(idOrigem, bigGraph);
        int topDistance = 0;
        Graph<Location, Integer> graph = getGraph(bigGraph);
        ArrayList<LinkedList<Location>> locationPaths = new ArrayList<>();
        ArrayList<Integer> locationDistance = new ArrayList<>();
        ArrayList<LinkedList<LocalTime>> arriveTimes = new ArrayList<>();
        ArrayList<LinkedList<LocalTime>> departTimes = new ArrayList<>();
        ArrayList<LinkedList<LocalTime>> afterChargeTimes = new ArrayList<>();
        ArrayList<LinkedList<LocalTime>> descargaTimes = new ArrayList<>();
        BinaryOperator<Integer> subtract = (a, b) -> a - b;
        LocalTime maxHour = getMaxHourToSearch(bigGraph);
        Algorithms.shortestPathsConstrained(graph, curLocation, Integer::compare, Integer::sum, subtract, 0,
                locationPaths, locationDistance, arriveTimes, departTimes, afterChargeTimes, descargaTimes, autonomy,
                time, velocity, maxHour);
        int maxHubs = -1;
        for (int i = 0; i < locationPaths.size(); i++) {
            int totalHubs = 0;
            for (int j = 0; j < locationPaths.get(i).size(); j++) {
                if (locationPaths.get(i).get(j).isHub()) {
                    totalHubs++;
                }
            }
            if (totalHubs > maxHubs) {
                maxHubs = totalHubs;
                topPath.clear();
                topArriveTimes.clear();
                topDepartTimes.clear();
                topAfterChargeTimes.clear();
                topDescargaTimes.clear();
                topDistance = 0;
                for (int j = 0; j < locationPaths.get(i).size(); j++) {
                    topPath.add(locationPaths.get(i).get(j));
                    topArriveTimes.add(arriveTimes.get(i).get(j));
                    topDepartTimes.add(departTimes.get(i).get(j));
                    topAfterChargeTimes.add(afterChargeTimes.get(i).get(j));
                    topDescargaTimes.add(descargaTimes.get(i).get(j));
                    topDistance = locationDistance.get(i);
                }
            }
        }
        return topDistance;
    }

    private LocalTime getMaxHourToSearch(Boolean bigGraph) {
        LocalTime maxHour = LocalTime.of(0, 1);
        for (Location location : graphRepository.getGraph(bigGraph).vertices()) {
            if (location.getEndHour() != null && location.getEndHour().isAfter(maxHour)) {
                maxHour = location.getEndHour();
            }
        }
        return maxHour;
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
                    Integer weight = (originalGraph.edge(location, adjLocation).getWeight() / 10000);
                    filteredGraph.addEdge(location, adjLocation, weight);
                }
            }
        }

        return filteredGraph;
    }

    public Pair<Integer, MapGraph<Location, Integer>> maximumCapacity(MapGraph<Location, Integer> graph,
            Location origin, Location destination) {

        Pair<Integer, Map<Location, Map<Location, Integer>>> result = Algorithms.fordFulkerson(graph, origin,
                destination);

        Map<Location, Map<Location, Integer>> residualGraph = result.getSecond();

        MapGraph<Location, Integer> maxFlowGraph = new MapGraph<>(true);

        for (Location vertex : residualGraph.keySet()) {
            for (Location adjVertex : residualGraph.get(vertex).keySet()) {
                Edge<Location, Integer> edge = graph.edge(vertex, adjVertex);
                if (edge != null) {
                    Integer originalCapacity = edge.getWeight();
                    Integer residualCapacity = residualGraph.get(vertex).get(adjVertex);
                    if (originalCapacity != null && residualCapacity != null) {
                        Integer flowOnEdge = originalCapacity - residualCapacity;
                        if (flowOnEdge > 0) {
                            maxFlowGraph.addVertex(vertex);
                            maxFlowGraph.addVertex(adjVertex);
                            maxFlowGraph.addEdge(vertex, adjVertex, flowOnEdge);
                        } else if (flowOnEdge < 0) {
                            maxFlowGraph.addVertex(adjVertex);
                            maxFlowGraph.addVertex(vertex);
                            maxFlowGraph.addEdge(adjVertex, vertex, -flowOnEdge);
                        }
                    }
                }
            }
        }

        return new Pair<>(result.getFirst(), maxFlowGraph);
    }

    public MapGraph<Location, Integer> transformToDirectedOneWay(MapGraph<Location, Integer> undirectedGraph) {
        MapGraph<Location, Integer> directedGraph = new MapGraph<>(false);

        for (Location location : undirectedGraph.vertices()) {
            directedGraph.addVertex(location);
            for (Location adjLocation : undirectedGraph.adjVertices(location)) {
                if (location.getId().compareTo(adjLocation.getId()) < 0) {
                    Integer weight = undirectedGraph.edge(location, adjLocation).getWeight();
                    directedGraph.addEdge(location, adjLocation, weight);
                }
            }
        }

        return directedGraph;
    }

    public boolean checkHours(LocalTime time, boolean bigGraph) {
        LocalTime minHour = LocalTime.of(23, 59);
        LocalTime maxHour = LocalTime.of(0, 0);
        for (Location location : graphRepository.getGraph(bigGraph).vertices()) {
            LocalTime startHour = location.getStartHour();
            LocalTime endHour = location.getEndHour();
            if (startHour != null && endHour != null) {
                if (startHour.isBefore(minHour)) {
                    minHour = startHour;
                }
                if (endHour.isAfter(maxHour)) {
                    maxHour = endHour;
                }
            }
        }
        if (time.isAfter(minHour) && time.isBefore(maxHour)) {
            return false;
        }
        System.out.println("O serviço ainda não começou! Insira uma hora entre " + minHour + " e " + maxHour + "");
        return true;
    }

    public List<Location> deliveryCircuitPath(String idOrigem, int nHubs, Boolean bigGraph) {
        if (!idExists(idOrigem, bigGraph)) {
            System.out.println("A localização de origem não existe!");
            return null;
        }
        if (nHubs < 0 || nHubs > 7 || nHubs < 5) {
            System.out.println("O número de hubs tem de ser entre 5 e 7!");
            return null;
        }

        try {
            List<List<Location>> paths = Algorithms.allPathsN(graphRepository.getGraph(bigGraph),
                    graphRepository.locationById(idOrigem, bigGraph), graphRepository.locationById(idOrigem, bigGraph),
                    nHubs);
            int maxCollaborations = 0;
            int minDistance = Integer.MAX_VALUE;
            List<Location> bestPath = new ArrayList<>();
            for (List<Location> path : paths) {
                int totalCollaborations = 0;
                int totalDistance = 0;
                for (int i = 0; i < path.size() - 1; i++) {
                    if (i < path.size() - 1) {
                        Location location1 = path.get(i);
                        Location location2 = path.get(i + 1);
                        totalCollaborations += location1.getNumEmployees();
                        totalDistance += graphRepository.distanceLocations(location1, location2, bigGraph);
                    }
                }
                if (totalCollaborations > maxCollaborations) {
                    maxCollaborations = totalCollaborations;
                    minDistance = totalDistance;
                    bestPath = path;
                } else if (totalCollaborations == maxCollaborations && totalDistance < minDistance) {
                    minDistance = totalDistance;
                    bestPath = path;
                }
            }
            return bestPath;
        } catch (Exception e) {
            System.out.println("Não foi possível encontrar um caminho :" + e.getMessage());
            return null;
        }
    }

    public int getDistance(Location location, Location location1, Boolean bigGraph) {
        return graphRepository.distanceLocations(location, location1, bigGraph);
    }

    public Pair<Duration, Integer> getChargeDuration(List<Location> bestPath, Boolean bigGraph, int autonomy) {
        int duringAutonomy = autonomy;
        Duration chargeDuration = Duration.ofMinutes(0);
        int numCharges = 0;
        for (int i = 0; i < bestPath.size() - 1; i++) {
            Location location1 = bestPath.get(i);
            Location location2 = bestPath.get(i + 1);
            int distance = graphRepository.distanceLocations(location1, location2, bigGraph);
            if (distance > duringAutonomy) {
                chargeDuration = chargeDuration.plus(chargingTime(autonomy, duringAutonomy));
                duringAutonomy = autonomy;
                numCharges++;
            }
            duringAutonomy -= distance;
        }
        return new Pair<>(chargeDuration, numCharges);
    }

    public Duration getTravDuration(List<Location> bestPath, Boolean bigGraph, int velocity) {
        Duration travelDuration = Duration.ofMinutes(0);
        int distance = 0;
        for (int i = 0; i < bestPath.size() - 1; i++) {
            Location location1 = bestPath.get(i);
            Location location2 = bestPath.get(i + 1);
            distance = graphRepository.distanceLocations(location1, location2, bigGraph);
            travelDuration = travelDuration.plus(travelTime(distance / 1000, velocity));
        }
        return travelDuration;
    }

    private Duration chargingTime(int autonomy, int leftAutonomy) {
        double gainedAutonomyPerMinute = 1016.6666666667;
        return Duration.ofMinutes(Math.round((autonomy - leftAutonomy) / gainedAutonomyPerMinute));
    }

    private Duration travelTime(int distance, int velocity) {
        return Duration.ofMinutes((long) (60 * ((double) distance / velocity)));
    }
}