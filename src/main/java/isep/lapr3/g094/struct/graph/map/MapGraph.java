package isep.lapr3.g094.struct.graph.map;

import isep.lapr3.g094.struct.graph.Edge;
import isep.lapr3.g094.struct.graph.CommonGraph;
import isep.lapr3.g094.struct.graph.Graph;

import java.util.*;

public class MapGraph<V, E> extends CommonGraph<V, E> {

    final private Map<V, MapVertex<V, E>> mapVertices;

    public MapGraph(boolean directed) {
        super(directed);
        mapVertices = new LinkedHashMap<>();
    }

    public MapGraph(Graph<V, E> g) {
        this(g.isDirected());
        copy(g, this);
    }

    @Override
    public boolean validVertex(V vert) {
        return (mapVertices.get(vert) != null);
    }

    @Override
    public Collection<V> adjVertices(V vert) {
        if (!validVertex(vert))
            return null;

        MapVertex<V, E> mv = mapVertices.get(vert);
        Collection<V> adjVertices = new ArrayList<>();
        for (Edge<V, E> edge : mv.getAllOutEdges()) {
            adjVertices.add(edge.getVDest());
        }
        return adjVertices;
    }

    @Override
    public Collection<Edge<V, E>> edges() {

        ArrayList<Edge<V, E>> le = new ArrayList<>(numEdges);

        for (MapVertex<V, E> mv : mapVertices.values())
            le.addAll(mv.getAllOutEdges());

        return le;
    }

    public Set<V> getVertices() {
        return new HashSet<>(mapVertices.keySet());
    }

    @Override
    public Edge<V, E> edge(V vOrig, V vDest) {

        if (!validVertex(vOrig) || !validVertex(vDest))
            return null;

        MapVertex<V, E> mv = mapVertices.get(vOrig);

        return mv.getEdge(vDest);
    }

    @Override
    public Edge<V, E> edge(int vOrigKey, int vDestKey) {
        V vOrig = vertex(vOrigKey);
        V vDest = vertex(vDestKey);

        return edge(vOrig, vDest);
    }

    @Override
    public int outDegree(V vert) {

        if (!validVertex(vert))
            return -1;

        MapVertex<V, E> mv = mapVertices.get(vert);

        return mv.numAdjVerts();
    }

    @Override
    public int inDegree(V vert) {

        if (!validVertex(vert))
            return -1;

        int degree = 0;
        for (V otherVert : mapVertices.keySet())
            if (edge(otherVert, vert) != null)
                degree++;

        return degree;
    }

    @Override
    public Collection<Edge<V, E>> outgoingEdges(V vert) {

        if (!validVertex(vert))
            return null;

        MapVertex<V, E> mv = mapVertices.get(vert);

        return mv.getAllOutEdges();
    }

    @Override
    public Collection<Edge<V, E>> incomingEdges(V vert) {
        if (!validVertex(vert))
            return null;

        Collection<Edge<V, E>> incomingEdges = new ArrayList<>();
        for (MapVertex<V, E> mv : mapVertices.values()) {
            for (Edge<V, E> edge : mv.getAllOutEdges()) {
                if (edge.getVDest().equals(vert)) {
                    incomingEdges.add(edge);
                }
            }
        }
        return incomingEdges;
    }

    @Override
    public boolean addVertex(V vert) {

        if (vert == null)
            throw new RuntimeException("Vértices não podem ser null!");
        if (validVertex(vert))
            return false;

        MapVertex<V, E> mv = new MapVertex<>(vert);
        vertices.add(vert);
        mapVertices.put(vert, mv);
        numVerts++;

        return true;
    }

    @Override
    public boolean addEdge(V vOrig, V vDest, E weight) {

        if (vOrig == null || vDest == null)
            throw new RuntimeException("Vértices não podem ser null!");
        if (edge(vOrig, vDest) != null)
            return false;

        if (!validVertex(vOrig))
            addVertex(vOrig);

        if (!validVertex(vDest))
            addVertex(vDest);

        MapVertex<V, E> mvo = mapVertices.get(vOrig);
        MapVertex<V, E> mvd = mapVertices.get(vDest);

        Edge<V, E> newEdge = new Edge<>(mvo.getElement(), mvd.getElement(), weight);
        mvo.addAdjVert(mvd.getElement(), newEdge);
        numEdges++;

        if (!isDirected)
            if (edge(vDest, vOrig) == null) {
                Edge<V, E> otherEdge = new Edge<>(mvd.getElement(), mvo.getElement(), weight);
                mvd.addAdjVert(mvo.getElement(), otherEdge);
                numEdges++;
            }

        return true;
    }

    @Override
    public boolean removeVertex(V vert) {

        if (vert == null)
            throw new RuntimeException("Vértices não podem ser null!");
        if (!validVertex(vert))
            return false;

        for (Edge<V, E> edge : incomingEdges(vert)) {
            removeEdge(edge.getVOrig(), vert);
        }

        MapVertex<V, E> mv = mapVertices.get(vert);

        numEdges -= mv.numAdjVerts();
        mapVertices.remove(vert);
        vertices.remove(vert);

        numVerts--;

        return true;
    }

    @Override
    public boolean removeEdge(V vOrig, V vDest) {

        if (vOrig == null || vDest == null)
            throw new RuntimeException("Vértices não podem ser null!");
        if (!validVertex(vOrig) || !validVertex(vDest))
            return false;

        Edge<V, E> edge = edge(vOrig, vDest);

        if (edge == null)
            return false;

        MapVertex<V, E> mvo = mapVertices.get(vOrig);

        mvo.remAdjVert(vDest);
        numEdges--;

        if (!isDirected) {
            edge = edge(vDest, vOrig);
            if (edge != null) {
                MapVertex<V, E> mvd = mapVertices.get(vDest);
                mvd.remAdjVert(vOrig);
                numEdges--;
            }
        }
        return true;
    }

    @Override
    public MapGraph<V, E> clone() {

        MapGraph<V, E> g = new MapGraph<>(this.isDirected);

        copy(this, g);

        return g;
    }

    @Override
    public String toString() {
        String s;
        if (numVerts == 0) {
            s = "\nGrafo não definido!";
        } else {
            s = "Grafo: " + numVerts + " vértices, " + numEdges + " arestas\n";
            for (MapVertex<V, E> mv : mapVertices.values())
                s += mv + "\n";
        }
        return s;
    }
}