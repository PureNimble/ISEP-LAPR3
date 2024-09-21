package isep.lapr3.g094.domain.type;

import java.util.List;
import isep.lapr3.g094.domain.Pair;

public class FurthestPoints {
    private Pair<Location, Location> pair;
    private List<Location> locations;
    private List<Integer> distances;

    public FurthestPoints(Pair<Location, Location> pair, List<Location> locations, List<Integer> distances) {
        this.pair = pair;
        this.locations = locations;
        this.distances = distances;
    }

    public Pair<Location, Location> getPair() {
        return pair;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Integer> getDistances() {
        return distances;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pair == null) ? 0 : pair.hashCode());
        result = prime * result + ((locations == null) ? 0 : locations.hashCode());
        result = prime * result + ((distances == null) ? 0 : distances.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FurthestPoints other = (FurthestPoints) obj;
        if (pair == null) {
            if (other.pair != null)
                return false;
        } else if (!pair.equals(other.pair))
            return false;
        if (locations == null) {
            if (other.locations != null)
                return false;
        } else if (!locations.equals(other.locations))
            return false;
        if (distances == null) {
            if (other.distances != null)
                return false;
        } else if (!distances.equals(other.distances))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FurthestPoints [pair=" + pair + ", locations=" + locations + ", distances=" + distances + "]";
    }

    
}