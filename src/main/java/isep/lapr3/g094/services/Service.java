package isep.lapr3.g094.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
        return Algorithms.allPaths(graphRepository.getBasketDistribution(), new Location(idOrigin), new Location(idDest));
    }

    public <V, E> Integer shortestPath(String idOrigin, String idDest, LinkedList<Location> shortPath) {
        return Algorithms.shortestPath(graphRepository.getBasketDistribution(), new Location(idOrigin), new Location(idDest), Integer::compare, Integer::sum, 0, shortPath);
    }

    public <V, E> boolean shortestPaths(String id) {
        return Algorithms.shortestPaths(graphRepository.getBasketDistribution(), new Location(id), Integer::compare, Integer::sum, 0, new ArrayList<LinkedList<Location>>(), new ArrayList<Integer>());
    }

    public int getLocationDegree(String id) {
        return graphRepository.getBasketDistribution().inDegree(new Location(id));
    }

    public MapGraph<Location, Integer> getBasketDistribution() {
        return graphRepository.getBasketDistribution();
    }
}