package isep.lapr3.g094.services;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public boolean createLocation(List<String> locations) {
        boolean allAdded = true;
        for (String location : locations) {
            String[] line = location.split(",");
            if (!graphRepository.addLocation(new Location(line[0], Double.parseDouble(line[1].replace(',', '.')),
                    Double.parseDouble(line[2].replace(',', '.')), Integer.parseInt(line[0].substring(2))))) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    public boolean addDistance(List<String> distances) {
        boolean allAdded = true;
        for (String distance : distances) {
            String[] line = distance.split(",");
            if (!graphRepository.addDistance(graphRepository.locationById(line[0]),
                    graphRepository.locationById(line[1]),
                    Integer.parseInt(line[2])))
                allAdded = false;
        }
        return allAdded;

    }

    public int keyLocation(Location location) {
        return graphRepository.keyLocation(location);
    }

    public Location locationByKey(int key) {
        return graphRepository.locationByKey(key);
    }

    public Location locationById(String id) {
        return graphRepository.locationById(id);
    }

    public Integer distanceLocations(String id1, String id2) {
        Location location1 = graphRepository.locationById(id1);
        Location location2 = graphRepository.locationById(id2);
        return graphRepository.distanceLocations(location1, location2);
    }

    public int getNumLocations() {
        return graphRepository.getNumLocations();
    }

    public int getNumDistances() {
        return graphRepository.getNumDistances();
    }

    public MapGraph<Location, Integer> getBasketDistribution() {
        return graphRepository.getBasketDistribution();
    }

    public Map<Location, Criteria> getVerticesIdeais() {
        Map<Location, Criteria> map = new HashMap<>();
        int numberMinimumPaths = 0;
        int i;
        for (Location location : getBasketDistribution().vertices()) {
            int degree = graphRepository.getBasketDistribution().inDegree(location);
            ArrayList<LinkedList<Location>> locationPaths = new ArrayList<>();
            ArrayList<Integer> locationDistance = new ArrayList<>();
            Algorithms.shortestPaths(graphRepository.getBasketDistribution(), location, Integer::compare, Integer::sum,
                    0, locationPaths, locationDistance);

            List<Integer> indices = IntStream.range(0, locationDistance.size()).mapToObj(Integer::valueOf)
                    .collect(Collectors.toList());
            indices.sort(Comparator.comparing(locationDistance::get));
            List<LinkedList<Location>> sortedLocationPaths = indices.stream()
                    .map(locationPaths::get)
                    .collect(Collectors.toList());
            locationDistance.sort(Integer::compareTo);
            locationPaths.clear();
            locationPaths.addAll(sortedLocationPaths);
            locationPaths.remove(0);
            locationDistance.remove(0);
            Criteria criteria = new Criteria(degree, locationPaths, numberMinimumPaths, locationDistance);
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
        map = sortByValue(map);
        return map;
    }

    private static Map<Location, Criteria> sortByValue(Map<Location, Criteria> map) {
        // Convert the map to a list of entries
        List<Map.Entry<Location, Criteria>> list = new ArrayList<>(map.entrySet());

        // Sort the list using a custom comparator
        list.sort(Comparator.comparing((Map.Entry<Location, Criteria> entry) -> entry.getValue().getDegree())
                .thenComparing(entry -> entry.getValue().getNumberMinimumPaths()).reversed());

        // Convert the sorted list back to a map
        Map<Location, Criteria> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Location, Criteria> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public Pair<FurthestPoints, Pair<List<Location>, Integer>> getMinimal(int autonomy) {
        // distribution graph
        MapGraph<Location, Integer> distributionGraph = graphRepository.getBasketDistribution();
        // furthest points
        Pair<Location, Location> furthestPoints = Algorithms.furthestPoints(distributionGraph,
                Integer::compare, Integer::sum, 0);
        LinkedList<Location> shortPath = new LinkedList<>();
        int distance = Algorithms.shortestPath(distributionGraph, furthestPoints.getFirst(),
                furthestPoints.getSecond(), Integer::compare, Integer::sum, 0, shortPath);
        int distanceAutonomy = 0;
        List<Location> rechargeLocations = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();
        rechargeLocations.add(shortPath.getFirst());
        for (int i = 0; i < shortPath.size() - 1; i++) {
            Location location1 = shortPath.get(i);
            Location location2 = shortPath.get(i + 1);
            int distanceBetweenPoints = distributionGraph.edge(location1, location2).getWeight();
            distances.add(distanceBetweenPoints);
            if (distanceBetweenPoints > autonomy) {
                rechargeLocations.add(null);
            }
            distanceAutonomy += distanceBetweenPoints;
            if (distanceAutonomy > autonomy) {
                rechargeLocations.add(location1);
                distanceAutonomy = 0;
            }
        }

        Pair<FurthestPoints, Pair<List<Location>, Integer>> output = new Pair<>(
                new FurthestPoints(furthestPoints, shortPath, distances), new Pair<>(rechargeLocations, distance));
        return output;
    }

    public Map<Location, Map<Location, Integer>> getMinimalPaths() {
        Map<Location, Map<Location, Integer>> map = new HashMap<>();
        MapGraph<Location, Integer> graph = graphRepository.getBasketDistribution();
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

    public List<Graph<Location, Integer>> divideIntoClusters(List<String> idsSelected){
        Set<Location> listHubs = new LinkedHashSet<>();
        for (String id : idsSelected) {
            for (Location location : graphRepository.getBasketDistribution().vertices()) {
                if (id.equals(location.getId())) {
                    listHubs.add(location);
                    break;
                }
            }
        }
        Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(graphRepository.getBasketDistribution());
        LinkedList<Location> shortPath = new LinkedList<>();
        return  Algorithms.divideGraph(minDistGraph, listHubs, Integer::compare, Integer::sum, 0, shortPath);
    }

    public float getCoefSil(List<Graph<Location, Integer>> clusters){
        LinkedList<Location> shortPath = new LinkedList<>();
        Graph<Location, Integer> minDistGraph = Algorithms.minSpanningTree(graphRepository.getBasketDistribution());
        return Algorithms.getSC(clusters, Integer::compare, Integer::sum, 0, shortPath, minDistGraph);
    }
}