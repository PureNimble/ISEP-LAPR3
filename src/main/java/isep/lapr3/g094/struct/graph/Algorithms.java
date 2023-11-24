package isep.lapr3.g094.struct.graph;

import isep.lapr3.g094.domain.Pair;
import isep.lapr3.g094.struct.graph.matrix.MatrixGraph;

import java.util.*;
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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

    public static <V, E> List<MatrixGraph<V, E>> divideGraph(MatrixGraph<V, E> g, Set<V> verticeList){
        int numVertices = g.numVertices();
        List<MatrixGraph<V, E>> clusterList = new ArrayList<>();
        for (V vertice : verticeList){
            MatrixGraph<V, E> matrixGraph = new MatrixGraph<>(false, numVertices);
            matrixGraph.addVertex(vertice);
            clusterList.add(matrixGraph);
        }
        boolean changed = true;
        while(changed){
            changed = false;
            for (V vertice : g.vertices){
                int maxClusterIndex = -1;
                int maxNeighborCount = -1;
                for (int i = 0; i < clusterList.size(); i++){
                    int neighborCount = countCommonNeighbors(vertice, clusterList.get(i), g);
                    if(maxNeighborCount < neighborCount){
                        maxNeighborCount = neighborCount;
                        maxClusterIndex = i;
                    }
                }
                if (!clusterList.get(maxClusterIndex).vertices.contains(vertice)){
                    for (MatrixGraph<V, E> veMatrixGraph : clusterList) {
                        veMatrixGraph.removeVertex(vertice);
                    }
                    clusterList.get(maxClusterIndex).addVertex(vertice);
                    changed = true;
                }
            }
        }
        fillEdges(clusterList, g);
        return clusterList;
    }

    private static <V, E> int countCommonNeighbors(V vertex, MatrixGraph<V, E> cluster, MatrixGraph<V, E> graph){
        int count = 0;
        for (V neighbor : graph.adjVertices(vertex)){
            if (cluster.vertices.contains(neighbor)){
                count++;
            }
        }
        return count;
    }

    private static <V, E> void fillEdges(List<MatrixGraph<V, E>> clusterList, MatrixGraph<V, E> graph){
        for (MatrixGraph<V, E> cluster : clusterList) {
            for (V vertice : cluster.vertices){
                for (V dest : graph.adjVertices(vertice)){
                    if(cluster.validVertex(dest)){
                        if (cluster.edge(dest, vertice) == null && cluster.edge(vertice, dest) == null){
                            E weight = graph.edge(vertice, dest).getWeight();
                            cluster.addEdge(vertice, dest, weight);
                        }
                    }
                }
            }
        }
    }

    public static <V, E> float getSC(List<MatrixGraph<V, E>> clusterList, Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                      LinkedList<V> shortPath, MatrixGraph<V, E> g){
        List<Float> sillouetteAverages = new ArrayList<>();
        for (MatrixGraph<V, E> cluster : clusterList){
            float sillouetteSum = 0;
            float numSillouettes = 0;
            float lowAvgDistOut = Float.MAX_VALUE;
            float sumDistIn = 0;
            for(V vertice : cluster.vertices){
                for(V verticeIn : cluster.vertices){
                    if (!vertice.equals(verticeIn)){
                        sumDistIn += (Float) shortestPath(cluster, vertice, verticeIn, ce, sum, zero, shortPath);
                    }
                }
                float avgDistIn = sumDistIn/(cluster.numVerts - 1);
                for (MatrixGraph<V, E> clusterout : clusterList){
                    float sumDistOut = 0;
                    if (!cluster.equals(clusterout)){
                        for (V outVert : clusterout.vertices){
                            sumDistOut += (Float) shortestPath(g, vertice, outVert, ce, sum, zero, shortPath);
                        }
                    }
                    float avgDistOut = sumDistOut/clusterout.numVerts;
                    if (avgDistOut < lowAvgDistOut){
                        lowAvgDistOut = avgDistOut;
                    }
                }
                sillouetteSum += calculateSillouette(lowAvgDistOut, avgDistIn);
                numSillouettes++;

            }
            sillouetteAverages.add(sillouetteSum/numSillouettes);
        }
        if(sillouetteAverages.stream().max(Float::compareTo).isPresent()){
            return sillouetteAverages.stream().max(Float::compareTo).get();
        } else {
            return 0;
        }
    }

    private static float calculateSillouette(float lowAvgDistOut, float avgDistIn){
        if (avgDistIn < lowAvgDistOut){
            return (1 - (avgDistIn/lowAvgDistOut));
        } else if (avgDistIn > lowAvgDistOut){
            return ((lowAvgDistOut/avgDistIn) - 1);
        } else {
            return 0;
        }
    }

    public static <V, E extends Comparable<E>> Graph<V, E> minSpanningTree(Graph<V, E> g) {
        PriorityQueue<Edge<V, E>> pq = new PriorityQueue<>(Comparator.comparing(Edge::getWeight));
        Set<V> visited = new HashSet<>();
        Graph<V, E> mst = new MatrixGraph<>(false, g.numVertices());

        V start = g.vertices().get(0);
        visited.add(start);

        for (Edge<V, E> edge : g.outgoingEdges(start)) {
            pq.add(edge);
        }

        while (!pq.isEmpty()) {
            Edge<V, E> edge = pq.poll();
            V v = edge.getVDest();
            if (!visited.contains(v)) {
                visited.add(v);
                mst.addEdge(edge.getVOrig(), v, edge.getWeight());
                for (Edge<V, E> e : g.outgoingEdges(v)) {
                    if (!visited.contains(e.getVDest())) {
                        pq.add(e);
                    }
                }
            }
        }

        return mst;
    }

    public static <V, E extends Comparable<E>> Pair<V, V> furthestPoints(Graph<V, E> g, Comparator<E> ce, BinaryOperator<E> sum, E zero) {
        E maxDist = zero;
        Pair<V, V> furthestPoints = null;
        for (V location1 : g.vertices()) {
            for (V location2 : g.vertices()) {
                if (!location1.equals(location2)) {
                    LinkedList<V> shortPath = new LinkedList<>();
                    E dist = shortestPath(g, location1, location2, ce, sum, zero, shortPath);
                    if (dist != null && dist.compareTo(maxDist) > 0) {
                        maxDist = dist;
                        furthestPoints = new Pair<>(location1, location2);
                    }
                }
            }
        }
        return furthestPoints;
    }

}