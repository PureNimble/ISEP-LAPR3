package isep.lapr3.g094.struct.graph;

import isep.lapr3.g094.struct.graph.matrix.MatrixGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.BinaryOperator;

public class Algorithms {

    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {

        if (!g.validVertex(vert)) {
            return (LinkedList<V>) Collections.<V>emptyList();
        }

        LinkedList<V> bfs = new LinkedList<>();
        Queue<V> queue = new LinkedList<>();
        Set<V> visited = new HashSet<>();

        queue.add(vert);
        visited.add(vert);

        while (!queue.isEmpty()) {
            V current = queue.poll();
            bfs.add(current);

            for (V adj : g.adjVertices(current)) {
                if (visited.add(adj)) {
                    queue.add(adj);
                }
            }
        }

        return bfs;
    }

    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {

        visited[g.key(vOrig)] = true;
        qdfs.add(vOrig);

        for (V adj : g.adjVertices(vOrig)) {
            if (!visited[g.key(adj)]) {
                DepthFirstSearch(g, adj, visited, qdfs);
            }
        }
    }

    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {

        if (!g.validVertex(vert)) {
            return (LinkedList<V>) Collections.<V>emptyList();
        }
        LinkedList<V> dfs = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];

        DepthFirstSearch(g, vert, visited, dfs);

        return dfs;
    }

    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, boolean[] visited,
            LinkedList<V> path, ArrayList<LinkedList<V>> paths) {

        visited[g.key(vOrig)] = true;
        path.add(vOrig);

        if (vOrig.equals(vDest)) {
            paths.add(new LinkedList<>(path));
        } else {
            for (V adj : g.adjVertices(vOrig)) {
                if (!visited[g.key(adj)]) {
                    allPaths(g, adj, vDest, visited, path, paths);
                }
            }
        }

        path.removeLast();
        visited[g.key(vOrig)] = false;
    }

    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {

        ArrayList<LinkedList<V>> paths = new ArrayList<>();
        LinkedList<V> path = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];

        allPaths(g, vOrig, vDest, visited, path, paths);

        return paths;
    }

    private static <V, E> void shortestPathDijkstra(Graph<V, E> g, V vOrig,
            Comparator<E> ce, BinaryOperator<E> sum, E zero,
            boolean[] visited, V[] pathKeys, E[] dist) {

        int vertices = g.numVertices();
        for (int i = 0; i < vertices; i++) {
            dist[i] = null;
            pathKeys[i] = null;
            visited[i] = false;
        }
        dist[g.key(vOrig)] = zero;

        while (vOrig != null) {
            visited[g.key(vOrig)] = true;

            for (V vAdj : g.adjVertices(vOrig)) {
                E edgeWeight = g.edge(vOrig, vAdj).getWeight();
                E oldDist = dist[g.key(vAdj)];
                E newDist = sum.apply(dist[g.key(vOrig)], edgeWeight);

                if (oldDist == null || ce.compare(newDist, oldDist) < 0) {
                    dist[g.key(vAdj)] = newDist;
                    pathKeys[g.key(vAdj)] = vOrig;
                }
            }

            vOrig = null;
            E minDist = null;
            for (V v : g.vertices()) {
                if (visited[g.key(v)] == false && dist[g.key(v)] != null) {
                    if (vOrig == null || ce.compare(dist[g.key(v)], minDist) < 0) {
                        minDist = dist[g.key(v)];
                        vOrig = v;
                    }
                }
            }
        }
    }

    public static <V, E> E shortestPath(Graph<V, E> g, V vOrig, V vDest,
            Comparator<E> ce, BinaryOperator<E> sum, E zero,
            LinkedList<V> shortPath) {

        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return null;
        }

        int vertices = g.numVertices();
        boolean[] visited = new boolean[vertices];
        V[] pathKeys = (V[]) new Object[vertices];
        E[] dist = (E[]) new Object[vertices];

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);

        shortPath.clear();
        if (!vOrig.equals(vDest) && pathKeys[g.key(vDest)] == null) {
            return null;
        }

        getPath(g, vOrig, vDest, pathKeys, shortPath);

        return dist[g.key(vDest)];
    }

    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig,
            Comparator<E> ce, BinaryOperator<E> sum, E zero,
            ArrayList<LinkedList<V>> paths, ArrayList<E> dists) {

        if (!g.validVertex(vOrig)) {
            return false;
        }

        int vertices = g.numVertices();
        boolean[] visited = new boolean[vertices];
        V[] pathKeys = (V[]) new Object[vertices];
        E[] dist = (E[]) new Object[vertices];

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);

        dists.clear();
        paths.clear();
        for (V v : g.vertices()) {
            dists.add(dist[g.key(v)]);
            LinkedList<V> shortPath = new LinkedList<>();
            getPath(g, vOrig, v, pathKeys, shortPath);
            paths.add(shortPath);
        }

        return true;
    }

    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest,
            V[] pathKeys, LinkedList<V> path) {

        if (!vOrig.equals(vDest) && pathKeys[g.key(vDest)] == null) {
            return;
        }

        path.push(vDest);
        V vControl = pathKeys[g.key(vDest)];

        while (vControl != null) {
            path.push(vControl);
            int key = g.key(vControl);

            if (key < 0) {
                path.removeFirst();
                return;
            }

            vControl = pathKeys[g.key(vControl)];
        }
    }

    public static <V, E> MatrixGraph<V, E> minDistGraph(Graph<V, E> g, Comparator<E> ce, BinaryOperator<E> sum) {
        int vertices = g.numVertices();
        E[][] dist = (E[][]) new Object[vertices][vertices];

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i == j) {
                    dist[i][j] = (E) Integer.valueOf(0);
                } else {
                    dist[i][j] = (E) Integer.valueOf(Integer.MAX_VALUE);
                }
            }
        }

        for (V v : g.vertices()) {
            for (V u : g.adjVertices(v)) {
                dist[g.key(v)][g.key(u)] = g.edge(v, u).getWeight();
            }
        }

        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (ce.compare(sum.apply(dist[i][k], dist[k][j]), dist[i][j]) < 0) {
                        dist[i][j] = sum.apply(dist[i][k], dist[k][j]);
                    }
                }
            }
        }

        MatrixGraph<V, E> minDistGraph = new MatrixGraph<>(false, vertices);
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                minDistGraph.addEdge(g.vertices().get(i), g.vertices().get(j), dist[i][j]);
            }
        }

        return minDistGraph;
    }

}