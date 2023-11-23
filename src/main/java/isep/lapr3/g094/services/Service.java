package isep.lapr3.g094.services;

import java.util.*;

import isep.lapr3.g094.domain.Pair;
import isep.lapr3.g094.domain.type.Criteria;
import isep.lapr3.g094.domain.type.FurthestPoints;
import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.repository.GraphRepository;
import isep.lapr3.g094.repository.Repositories;
import isep.lapr3.g094.struct.graph.Algorithms;
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

    public int keyLocation(String id) {
        Location location = new Location(id);
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

    public <V, E> LinkedList<Location> BreadthFirstSearch(String id) {
        return Algorithms.BreadthFirstSearch(graphRepository.getBasketDistribution(), new Location(id));
    }

    public <V, E> LinkedList<Location> DepthFirstSearch(String id) {
        return Algorithms.DepthFirstSearch(graphRepository.getBasketDistribution(), new Location(id));
    }

    public <V, E> ArrayList<LinkedList<Location>> allPaths(String idOrigin, String idDest) {
        return Algorithms.allPaths(graphRepository.getBasketDistribution(), new Location(idOrigin),
                new Location(idDest));
    }

    public <V, E> Integer shortestPath(String idOrigin, String idDest, LinkedList<Location> shortPath) {
        return Algorithms.shortestPath(graphRepository.getBasketDistribution(), new Location(idOrigin),
                new Location(idDest), Integer::compare, Integer::sum, 0, shortPath);
    }

    public <V, E> boolean shortestPaths(String id) {
        return Algorithms.shortestPaths(graphRepository.getBasketDistribution(), new Location(id), Integer::compare,
                Integer::sum, 0, new ArrayList<LinkedList<Location>>(), new ArrayList<Integer>());
    }

    public int getLocationDegree(String id) {
        return graphRepository.getBasketDistribution().inDegree(new Location(id));
    }

    public <V> Pair<Location, Location> furthestPoints() {
        return Algorithms.furthestPoints(graphRepository.getBasketDistribution(), Integer::compare, Integer::sum, 0);
    }

    public MapGraph<Location, Integer> getBasketDistribution() {
        return graphRepository.getBasketDistribution();
    }

    public Map<String, Criteria> getVerticesIdeais() {
        Map<String, Criteria> map = new HashMap<>();
        int numberMinimumPaths = 0;
        int i;
        for (Location location : getBasketDistribution().vertices()) {
            int degree = graphRepository.getBasketDistribution().inDegree(location);
            ArrayList<LinkedList<Location>> locationPaths = new ArrayList<>();
            ArrayList<Integer> locationDistance = new ArrayList<>();
            Algorithms.shortestPaths(graphRepository.getBasketDistribution(), location, Integer::compare, Integer::sum,
                    0, locationPaths, locationDistance);
            Criteria criteria = new Criteria(degree, locationPaths, numberMinimumPaths, locationDistance);
            map.put(location.getId(), criteria);
        }
        for (Map.Entry<String, Criteria> entry : map.entrySet()) {
            numberMinimumPaths = 0;
            String id = entry.getKey();
            for (Map.Entry<String, Criteria> entry2 : map.entrySet()) {
                if (!entry.getKey().equals(entry2.getKey())) {
                    for (i = 0; i < entry2.getValue().getPaths().size(); i++) {
                        if (entry2.getValue().getPaths().get(i).contains(new Location(id))) {
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

    public static Map<String, Criteria> sortByValue(Map<String, Criteria> map) {
        // Convert the map to a list of entries
        List<Map.Entry<String, Criteria>> list = new ArrayList<>(map.entrySet());

        // Sort the list using a custom comparator
        list.sort(Comparator.comparing((Map.Entry<String, Criteria> entry) -> entry.getValue().getDegree())
                .thenComparing(entry -> entry.getValue().getNumberMinimumPaths()).reversed());

        // Convert the sorted list back to a map
        Map<String, Criteria> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Criteria> entry : list) {
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
}