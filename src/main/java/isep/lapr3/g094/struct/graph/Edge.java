package isep.lapr3.g094.struct.graph;

import java.util.Objects;

public class Edge<V, E> {
    final private V vOrig;
    final private V vDest;
    private E weight;

    public Edge(V vOrig, V vDest, E weight) {
        if ((vOrig == null) || (vDest == null))
            throw new RuntimeException("Vértices não podem ser null!");
        this.vOrig = vOrig;
        this.vDest = vDest;
        this.weight = weight;
    }

    public V getVOrig() {
        return vOrig;
    }

    public V getVDest() {
        return vDest;
    }

    public E getWeight() {
        return weight;
    }

    public void setWeight(E weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s Valor: %s m\n", vOrig, vDest, weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked")
        Edge<V, E> edge = (Edge<V, E>) o;
        return vOrig.equals(edge.vOrig) &&
                vDest.equals(edge.vDest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vOrig, vDest);
    }
}
