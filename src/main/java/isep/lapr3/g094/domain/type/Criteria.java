package isep.lapr3.g094.domain.type;

import java.util.List;

public class Criteria {

    private int degree;

    private List<String> paths;

    private int numberMinimumPaths;

    public Criteria(int degree, List<String> paths, int numberMinimumPaths) {
        this.degree = degree;
        this.paths = paths;
        this.numberMinimumPaths = numberMinimumPaths;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public int getNumberMinimumPaths() {
        return numberMinimumPaths;
    }

    public void setNumberMinimumPaths(int numberMinimumPaths) {
        this.numberMinimumPaths = numberMinimumPaths;
    }

}
