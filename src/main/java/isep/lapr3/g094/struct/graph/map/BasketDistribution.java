package isep.lapr3.g094.struct.graph.map;

import isep.lapr3.g094.domain.Location;
import isep.lapr3.g094.struct.graph.Edge;

public class BasketDistribution {

    final private MapGraph<Location, Integer> basketDistribution;

    public BasketDistribution() {
        this.basketDistribution = new MapGraph<>(false);
    }

    public void addLocation(Location location) {
        this.basketDistribution.addVertex(location);
    }

    public void addDistance(Location location1, Location location2, Integer distance) {
        this.basketDistribution.addEdge(location1, location2, distance);
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

    @Override
    public String toString() {
        return this.basketDistribution.toString();
    }
}
