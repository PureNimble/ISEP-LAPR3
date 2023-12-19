package isep.lapr3.g094.application.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import isep.lapr3.g094.domain.Pair;
import isep.lapr3.g094.domain.type.Criteria;
import isep.lapr3.g094.domain.type.FurthestPoints;
import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.services.Service;
import isep.lapr3.g094.services.Services;
import isep.lapr3.g094.struct.graph.Graph;
import isep.lapr3.g094.struct.graph.map.MapGraph;

public class GraphController {

    private Service service;

    public GraphController() {
        getService();
    }

    private Service getService() {
        if (service == null) {
            Services services = Services.getInstance();

            service = services.getService();
        }
        return service;
    }

    public MapGraph<Location, Integer> getGraph(boolean bigGraph) {
        return service.getGraph(bigGraph);
    }

    public int getNumLocations(boolean bigGraph) {
        return service.getNumLocations(bigGraph);
    }

    public int getNumDistances(boolean bigGraph) {
        return service.getNumDistances(bigGraph);
    }

    public Location locationById(String id, boolean bigGraph) {
        return service.locationById(id, bigGraph);
    }

    public Integer distanceLocations(String id1, String id2, boolean bigGraph) {
        return service.distanceLocations(id1, id2, bigGraph);
    }

    public Map<Location, Criteria> getVerticesIdeais(int numberOfHubs, boolean bigGraph) {
        return service.getVerticesIdeais(numberOfHubs, bigGraph);
    }

    public Pair<FurthestPoints, Pair<List<Location>, Integer>> getMinimal(int autonomy, boolean bigGraph) {
        return service.getMinimal(autonomy, bigGraph);
    }

    public Map<Location, Map<Location, Integer>> getMinimalPaths(boolean bigGraph) {
        return service.getMinimalPaths(bigGraph);
    }

    public List<Graph<Location, Integer>> divideIntoClusters(List<String> idsSelected, boolean bigGraph) {
        return service.divideIntoClusters(idsSelected, bigGraph);
    }

    public float getCoefSil(List<Graph<Location, Integer>> clusters, boolean bigGraph) {
        return service.getCoefSil(clusters, bigGraph);
    }

    public ArrayList<LinkedHashMap<Location, Integer>> getAllPathsWithAutonomy(String origin, String destination,
            int autonomy,
            int velocity, boolean bigGraph) {
        return service.getAllPathsWithAutonomy(origin, destination, autonomy, velocity, bigGraph);
    }

    public boolean generateInitialDataCSV(boolean bigGraph) {
        return service.generateInitialDataCSV(bigGraph);
    }

    public boolean generateDataCSV(MapGraph<Location, Integer> graph) {
        return service.generateDataCSV(graph);
    }

    public boolean convertMapToMapGraph(Map<Location, Map<Location, Integer>> map, MapGraph<Location, Integer> graph) {
        return service.convertMapToMapGraph(map, graph);
    }

    public String getLatestFileFromDirectory(String string) {
        return service.getLatestFileFromDirectory(string);
    }

    public void maximizedPath(String idOrigem, LocalTime time, int autonomy, int velocity, Boolean bigGraph) {
        service.maximizedPath(idOrigem, time, autonomy, velocity, bigGraph);
    }
}