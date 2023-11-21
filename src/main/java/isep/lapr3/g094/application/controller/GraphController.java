package isep.lapr3.g094.application.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.services.Service;
import isep.lapr3.g094.services.Services;
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

    public int keyLocation(String id) {
        return service.keyLocation(id);
    }

    public Location locationByKey(int key) {
        return service.locationByKey(key);
    }

    public Location locationById(String id) {
        return service.locationById(id);
    }

    public Integer distanceLocations(String id1, String id2) {
        return service.distanceLocations(id1, id2);
    }

    public <V, E> LinkedList<Location> BreadthFirstSearch(String id) {
        return service.BreadthFirstSearch(id);
    }

    public <V, E> LinkedList<Location> DepthFirstSearch(String id) {
        return service.DepthFirstSearch(id);
    }

    public <V, E> ArrayList<LinkedList<Location>> allPaths(String idOrigin, String idDest) {
        return service.allPaths(idOrigin, idDest);
    }

    public <V, E> Integer shortestPath(String idOrigin, String idDest, LinkedList<Location> shortPath) {
        return service.shortestPath(idOrigin, idDest, shortPath);
    }

    public <V, E> boolean shortestPaths(String id) {
        return service.shortestPaths(id);
    }

    public int getLocationDegree(String id) {
        return service.getLocationDegree(id);
    }


}
