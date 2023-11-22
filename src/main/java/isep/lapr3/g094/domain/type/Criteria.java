package isep.lapr3.g094.domain.type;

import java.util.ArrayList;
import java.util.LinkedList;

public class Criteria {

    private int degree;

    private ArrayList<LinkedList<Location>> paths;

    private int numberMinimumPaths;

    private ArrayList<Integer> distances;

    public Criteria(int degree, ArrayList<LinkedList<Location>> paths, int numberMinimumPaths, ArrayList<Integer> distances) {
        this.degree = degree;
        this.paths = paths;
        this.numberMinimumPaths = numberMinimumPaths;
        this.distances = distances;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public ArrayList<LinkedList<Location>> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<LinkedList<Location>> paths) {
        this.paths = paths;
    }

    public int getNumberMinimumPaths() {
        return numberMinimumPaths;
    }

    public void setNumberMinimumPaths(int numberMinimumPaths) {
        this.numberMinimumPaths = numberMinimumPaths;
    }

    public ArrayList<Integer> getDistances() {
        return distances;
    }

    public void setDistances(ArrayList<Integer> distances) {
        this.distances = distances;
    }
}
