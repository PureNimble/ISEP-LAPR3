package isep.lapr3.g094.application.controller;

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

    public MapGraph<Location, Integer> getBasketDistribution() {
        return service.getBasketDistribution();
    }

    public int getNumLocations() {
        return service.getNumLocations();
    }

    public int getNumDistances() {
        return service.getNumDistances();
    }

    public Location locationById(String id) {
        return service.locationById(id);
    }

    public Integer distanceLocations(String id1, String id2) {
        return service.distanceLocations(id1, id2);
    }

    public Map<Location, Criteria> getVerticesIdeais() {
        return service.getVerticesIdeais();
    }

    public Pair<FurthestPoints, Pair<List<Location>, Integer>> getMinimal(int autonomy) {
        return service.getMinimal(autonomy);
    }

    public Map<Location, Map<Location, Integer>> getMinimalPaths() {
        return service.getMinimalPaths();
    }

    public List<Graph<Location, Integer>> divideIntoClusters(List<String> idsSelected) {
        return service.divideIntoClusters(idsSelected);
    }

    public float getCoefSil(List<Graph<Location, Integer>> clusters) {
        return service.getCoefSil(clusters);
    }
}