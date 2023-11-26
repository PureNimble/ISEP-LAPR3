package isep.lapr3.g094.repository;

import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.struct.graph.Edge;
import isep.lapr3.g094.struct.graph.map.MapGraph;

public class GraphRepository {

    private MapGraph<Location, Integer> basketDistribution;
    private MapGraph<Location, Integer> smallGraph;

    public GraphRepository() {
        this.basketDistribution = new MapGraph<>(false);
        this.smallGraph = new MapGraph<>(false);
    }

    public boolean addLocation(Location location, boolean bigGraph) {
        return bigGraph ? this.basketDistribution.addVertex(location) : this.smallGraph.addVertex(location);
    }

    public boolean addDistance(Location location1, Location location2, Integer distance, boolean bigGraph) {
        return bigGraph ? this.basketDistribution.addEdge(location1, location2, distance)
                : this.smallGraph.addEdge(location1, location2, distance);
    }

    public int keyLocation(Location location) {
        return this.basketDistribution.key(location);
    }

    public Location locationByKey(int key) {
        return this.basketDistribution.vertex(key);
    }

    public Location locationById(String id) {
        for (Location location : this.basketDistribution.vertices()) {
            if (location.getId().equals(id)) {
                return location;
            }
        }
        return null;
    }

    public Integer distanceLocations(Location location1, Location location2) {
        Edge<Location, Integer> distance = this.basketDistribution.edge(location1, location2);
        return distance != null ? distance.getWeight() : null;
    }

    public int getNumLocations() {
        return this.basketDistribution.numVertices();
    }

    public int getNumDistances() {
        return this.basketDistribution.numEdges();
    }

    public MapGraph<Location, Integer> getSmallGraph() {
        return smallGraph;
    }

    public MapGraph<Location, Integer> getBasketDistribution() {
        return basketDistribution;
    }

    @Override
    public String toString() {
        return this.basketDistribution.toString();
    }
}
