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

    public int keyLocation(Location location, boolean bigGraph) {
        return bigGraph ? this.basketDistribution.key(location) : this.smallGraph.key(location);
    }

    public Location locationByKey(int key, boolean bigGraph) {
        return bigGraph ? this.basketDistribution.vertex(key) : this.smallGraph.vertex(key);
    }

    public Location locationById(String id, boolean bigGraph) {
        if (bigGraph) {
            for (Location location : this.basketDistribution.vertices()) {
                if (location.getId().equals(id)) {
                    return location;
                }
            }
        } else {
            for (Location location : this.smallGraph.vertices()) {
                if (location.getId().equals(id)) {
                    return location;
                }
            }
        }
        return null;
    }

    public Integer distanceLocations(Location location1, Location location2, boolean bigGraph) {
        Edge<Location, Integer> distance = null;
        if (bigGraph) {
            if (this.basketDistribution.edge(location1, location2) == null) {
                return null;
            }
            distance = this.basketDistribution.edge(location1, location2);
        } else {
            if (this.smallGraph.edge(location1, location2) == null) {
                return null;
            }
            distance = this.smallGraph.edge(location1, location2);
        }

        return distance != null ? distance.getWeight() : null;
    }


    public int getNumLocations(boolean bigGraph) {
        return bigGraph ? this.basketDistribution.numVertices() : this.smallGraph.numVertices();
    }

    public int getNumDistances(boolean bigGraph) {
        return bigGraph ? this.basketDistribution.numEdges() : this.smallGraph.numEdges();
    }

    public MapGraph<Location, Integer> getGraph(boolean bigGraph) {
        return bigGraph ? this.basketDistribution : this.smallGraph;
    }
}
