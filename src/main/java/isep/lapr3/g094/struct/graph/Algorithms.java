package isep.lapr3.g094.struct.graph;

import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.domain.Pair;
import isep.lapr3.g094.struct.graph.matrix.MatrixGraph;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BinaryOperator;

public class Algorithms {

    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {

        if (!g.validVertex(vert)) {
            return null;
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

    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vert, LinkedList<V> qdfs){
        if (!g.validVertex(vert)) {
            return;
        }

        boolean[] visited = new boolean[g.numVertices()];

        DepthFirstSearch(g, vert, visited, qdfs);
    }

    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {

        if (!g.validVertex(vert)) {
            return null;
        }
        LinkedList<V> dfs = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];

        DepthFirstSearch(g, vert, visited, dfs);

        return dfs;
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

    public static <V, E> List<List<V>> allPathsWithLimit(Graph<V, E> g, V vOrig, V vDest, E distanceLimit,
            Comparator<E> ce, BinaryOperator<E> sum, E zero) {
        List<List<V>> paths = new ArrayList<>();

        depthFirstSearchWithDistanceLimit(g, vOrig, vDest, distanceLimit, ce, sum, zero,
                new ArrayList<>(Collections.singletonList(vOrig)), zero,
                paths, new HashSet<>());
        return paths;
    }

    public static <V, E> void depthFirstSearchWithDistanceLimit(Graph<V, E> g, V v, V vDest, E distanceLimit,
            Comparator<E> ce,
            BinaryOperator<E> sum, E weight,
            List<V> path, E pathWeight, List<List<V>> paths, Set<V> visited) {
        if (ce.compare(pathWeight, distanceLimit) > 0) {
            return;
        }

        if (v.equals(vDest)) {
            paths.add(new ArrayList<>(path));
        }

        visited.add(v); // Mark vertex as visited

        for (V vAdj : g.adjVertices(v)) {
            if (!visited.contains(vAdj)) { // Skip if vertex has been visited
                E edgeWeight = g.edge(v, vAdj).getWeight();
                E newPathWeight = sum.apply(pathWeight, edgeWeight);
                path.add(vAdj);
                depthFirstSearchWithDistanceLimit(g, vAdj, vDest, distanceLimit, ce, sum, weight, path, newPathWeight,
                        paths, new HashSet<>(visited)); // Pass a copy of visited set
                path.remove(path.size() - 1);
            }
        }
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


    @SuppressWarnings("unchecked")
    public static <V, E> boolean shortestPathsConstrained(Graph<V, E> g, V vOrig,
            Comparator<E> ce, BinaryOperator<E> sum, BinaryOperator<E> subtract, E zero,
            ArrayList<LinkedList<V>> paths, ArrayList<E> dists, ArrayList<LinkedList<LocalTime>> arriveTimes, ArrayList<LinkedList<LocalTime>> departTimes, ArrayList<LinkedList<LocalTime>> afterChargeTimes, ArrayList<LinkedList<LocalTime>> descargaTimes,E autonomy, LocalTime time, double velocity, LocalTime maxHour) {

        if (!g.validVertex(vOrig)) {
            return false;
        }
        int vertices = g.numVertices();
        boolean[] visited = new boolean[vertices];
        V[] pathKeys = (V[]) new Object[vertices];
        E[] dist = (E[]) new Object[vertices];
        LocalTime[] arriveTime = new LocalTime[vertices];
        LocalTime[] departTime = new LocalTime[vertices];
        LocalTime[] afterChargeTime = new LocalTime[vertices];
        LocalTime[] descargaTime = new LocalTime[vertices];
        LocalTime[] pathTimes = new LocalTime[vertices];
        E[] autonomies = (E[]) new Object[g.numVertices()];
        shortestPathDijkstraConstrained(g, vOrig, ce, sum, subtract, zero, visited, pathKeys, arriveTime, departTime, afterChargeTime, descargaTime, dist, autonomy, time, velocity, maxHour, autonomies, pathTimes);

        dists.clear();
        paths.clear();
        arriveTimes.clear();
        departTimes.clear();
        afterChargeTimes.clear();
        descargaTimes.clear();
        for (V v : g.vertices()) {
            LinkedList<V> shortPath = new LinkedList<>();
            getPath(g, vOrig, v, pathKeys, shortPath);
            if (!shortPath.isEmpty()) {
                dists.add(dist[g.key(v)]);
                paths.add(shortPath);
            }
            LinkedList<LocalTime> arriveTimePath = new LinkedList<>();
            getTimes(g, vOrig, v, pathKeys, arriveTimePath, arriveTime);
            if (!arriveTimePath.isEmpty()) {
                arriveTimes.add(arriveTimePath);
            }
            LinkedList<LocalTime> departTimePath = new LinkedList<>();
            getTimes(g, vOrig, v, pathKeys, departTimePath, departTime);
            if (!departTimePath.isEmpty()) {
                departTimes.add(departTimePath);
            }
            LinkedList<LocalTime> afterChargeTimePath = new LinkedList<>();
            getTimes(g, vOrig, v, pathKeys, afterChargeTimePath, afterChargeTime);
            if (!afterChargeTimePath.isEmpty()) {
                afterChargeTimes.add(afterChargeTimePath);
            }
            LinkedList<LocalTime> descargaTimePath = new LinkedList<>();
            getTimes(g, vOrig, v, pathKeys, descargaTimePath, descargaTime);
            if (!descargaTimePath.isEmpty()) {
                descargaTimes.add(descargaTimePath);
            }
        }

        return true;
    }

    private static <V, E> void shortestPathDijkstraConstrained(Graph<V, E> g, V vOrig,
            Comparator<E> ce, BinaryOperator<E> sum, BinaryOperator<E> subtract, E zero,
            boolean[] visited, V[] pathKeys, LocalTime[] arriveTimes, LocalTime[] departTimes, LocalTime[] afterChargeTimes, LocalTime[] descargaTimes, E[] dist, E autonomy, LocalTime time, double velocity, LocalTime maxHour, E[] autonomies, LocalTime[] pathTimes) {
        
        E duringAutonomy = autonomy;
        double gainedAutonomyPerMinute = 1016.6666666667;
        int vertices = g.numVertices();
        V vPrevious = null;
        for (int i = 0; i < vertices; i++) {
            dist[i] = null;
            pathKeys[i] = null;
            visited[i] = false;
        }
        dist[g.key(vOrig)] = zero;
        arriveTimes[g.key(vOrig)] = time;
        departTimes[g.key(vOrig)] = time;
        descargaTimes[g.key(vOrig)] = null;
        pathTimes[g.key(vOrig)] = LocalTime.of(0, 0);
        LocalTime pathTime = LocalTime.of(0, 0);
        LocalTime pathTimeMax = LocalTime.of(0, 0);
        pathTimeMax = pathTimeMax.plus(Duration.ofMinutes(maxHour.getHour() * 60 + maxHour.getMinute() - time.getHour() * 60 - time.getMinute()));

        while (vOrig != null) {
            visited[g.key(vOrig)] = true;
            LocalTime currentTime = departTimes[g.key(vOrig)];
            LocalTime currentPathTime = pathTimes[g.key(vOrig)];
            
            for (V vAdj : g.adjVertices(vOrig)) {
                if (vAdj.equals(vOrig)) {
                    continue;
                }
                time = currentTime;
                pathTime = currentPathTime;        
                E edgeWeight = g.edge(vOrig, vAdj).getWeight();
                if (ce.compare(edgeWeight, autonomy) > 0) {
                    continue;
                }
                E resetAutonomy = duringAutonomy;
                if (ce.compare(edgeWeight, duringAutonomy) > 0) {
                    time = time.plus(Duration.ofMinutes((long) (Math.round((float) ((Integer) subtract.apply(autonomy, duringAutonomy)).intValue()) / gainedAutonomyPerMinute)));
                    pathTime = pathTime.plus(Duration.ofMinutes((long) (Math.round((float) ((Integer) subtract.apply(autonomy, duringAutonomy)).intValue()) / gainedAutonomyPerMinute)));
                    duringAutonomy = autonomy;
                }
                LocalTime timeAfterCharge = time;
                time = time.plus(Duration.ofMinutes((long) (60 * ((double) ((Integer) edgeWeight).intValue() / 1000 / velocity))));
                pathTime = pathTime.plus(Duration.ofMinutes((long) (60 * ((double) ((Integer) edgeWeight).intValue() / 1000 / velocity))));            
                LocalTime endHour = ((Location) vAdj).getEndHour();
                if ((endHour != null && time.isAfter(endHour)) || time.isAfter(maxHour) || pathTime.isAfter(pathTimeMax)) {
                    duringAutonomy = resetAutonomy;
                    time = currentTime;
                    pathTime = currentPathTime;
                    continue;
                }
                LocalTime arrivedTime = time;
                if (((Location) vAdj).isHub() && ((Location) vAdj).getEndHour().isAfter(time) && ((Location) vAdj).getStartHour().isBefore(time)) {
                    time = time.plus(Duration.ofMinutes(new Random().nextInt(10) + 1));
                    pathTime = pathTime.plus(Duration.ofMinutes(new Random().nextInt(10) + 1));
                }
                LocalTime descargasTime = time;
                E oldDist = dist[g.key(vAdj)];
                E newDist = sum.apply(dist[g.key(vOrig)], edgeWeight);

                if (oldDist == null || ce.compare(newDist, oldDist) < 0) {
                    dist[g.key(vAdj)] = newDist;
                    pathKeys[g.key(vAdj)] = vOrig;
                    arriveTimes[g.key(vAdj)] = arrivedTime;
                    afterChargeTimes[g.key(vAdj)] = timeAfterCharge;
                    descargaTimes[g.key(vAdj)] = descargasTime;
                    departTimes[g.key(vAdj)] = time;

                    duringAutonomy = subtract.apply(duringAutonomy, edgeWeight);
                    autonomies[g.key(vAdj)] = duringAutonomy;
                    pathTimes[g.key(vAdj)] = pathTime;
                }
            }

            vPrevious = vOrig;
            vOrig = null;
            LocalTime minArriveTime = null;

            for (V v : g.adjVertices(vPrevious)) {
                if (visited[g.key(v)] == false && arriveTimes[g.key(v)] != null) {
                    if (vOrig == null || arriveTimes[g.key(v)].isBefore(minArriveTime) && arriveTimes[g.key(v)].isAfter(arriveTimes[g.key(vOrig)])) {
                        minArriveTime = arriveTimes[g.key(v)];
                        vOrig = v;
                    }
                }
            }

            if (vOrig != null) {
                duringAutonomy = autonomies[g.key(vOrig)];
                time = departTimes[g.key(vOrig)];
                pathTime = pathTimes[g.key(vOrig)];
            }
        }
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


    private static <V, E> void getTimes(Graph<V, E> g, V vOrig, V vDest,
                V[] pathKeys, LinkedList<LocalTime> arriveTimes, LocalTime[] times) {

        if (!vOrig.equals(vDest) && pathKeys[g.key(vDest)] == null) {
            return;
        }

        arriveTimes.push(times[g.key(vDest)]);
        V vControl = pathKeys[g.key(vDest)];

        while (vControl != null) {
            arriveTimes.push(times[g.key(vControl)]);
            int key = g.key(vControl);

            if (key < 0) {
                arriveTimes.removeFirst();
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

    public static <V, E> List<Graph<V, E>> divideGraph(Graph<V, E> g, Set<V> verticeList, Comparator<E> ce,
            BinaryOperator<E> sum, E zero,
            LinkedList<V> shortPath) {
        int numVertices = g.numVertices();
        List<Graph<V, E>> clusterList = new ArrayList<>();
        for (V vertice : verticeList) {
            Graph<V, E> graph = new MatrixGraph<>(false, numVertices);
            graph.addVertex(vertice);
            clusterList.add(graph);
        }
        for (V vertice : g.vertices()) {
            int maxClusterIndex = -1;
            int minDist = Integer.MAX_VALUE;
            int currentCluster = 0;
            for (V hub : verticeList) {
                int dist = getDistanceTwoVsMST(g, vertice, hub, ce, sum, zero, shortPath);
                if (minDist > dist) {
                    minDist = dist;
                    maxClusterIndex = currentCluster;
                }
                currentCluster++;
            }
            clusterList.get(maxClusterIndex).addVertex(vertice);
        }
        fillEdges(clusterList, g);
        return clusterList;
    }

    @SuppressWarnings("unused")
    private static <V, E> int countCommonNeighbors(V vertex, Graph<V, E> cluster, Graph<V, E> graph) {
        int count = 0;
        for (V neighbor : graph.adjVertices(vertex)) {
            if (cluster.vertices().contains(neighbor)) {
                count++;
            }
        }
        return count;
    }

    private static <V, E> void fillEdges(List<Graph<V, E>> clusterList, Graph<V, E> graph) {
        for (Graph<V, E> cluster : clusterList) {
            for (V vertice : cluster.vertices()) {
                for (V dest : graph.adjVertices(vertice)) {
                    if (cluster.validVertex(dest)) {
                        if (cluster.edge(dest, vertice) == null && cluster.edge(vertice, dest) == null) {
                            E weight = graph.edge(vertice, dest).getWeight();
                            cluster.addEdge(vertice, dest, weight);
                        }
                    }
                }
            }
        }
    }

    private static <V, E> int getDistanceTwoVsMST(Graph<V, E> graph, V vertOrigin, V vertDest, Comparator<E> ce,
            BinaryOperator<E> sum, E zero,
            LinkedList<V> shortPath) {
        shortestPath(graph, vertOrigin, vertDest, ce, sum, zero, shortPath);
        int dist = 0;
        for (int i = 0, u = 1; u < shortPath.size(); i++, u++) {
            dist += (int) graph.edge(shortPath.get(i), shortPath.get(u)).getWeight();
        }
        return dist;
    }

    public static <V, E> float getSC(List<Graph<V, E>> clusterList, Comparator<E> ce, BinaryOperator<E> sum, E zero,
            LinkedList<V> shortPath, Graph<V, E> originalGraph) {
        List<Float> sillouetteAverages = new ArrayList<>();
        for (Graph<V, E> cluster : clusterList) {
            float sillouetteSum = 0;
            float numSillouettes = 0;
            float lowAvgDistOut = Float.MAX_VALUE;
            float sumDistIn = 0;
            for (V vertice : cluster.vertices()) {
                for (V verticeIn : cluster.vertices()) {
                    if (!vertice.equals(verticeIn)) {
                        shortestPath(cluster, vertice, verticeIn, ce, sum, zero, shortPath);
                        int dist = 0;
                        for (int i = 0, u = 1; u < shortPath.size(); i++, u++) {
                            dist += (int) cluster.edge(shortPath.get(i), shortPath.get(u)).getWeight();
                        }
                        sumDistIn += dist;
                    }
                }
                float avgDistIn = sumDistIn / (cluster.numVertices() - 1);
                for (Graph<V, E> clusterout : clusterList) {
                    float sumDistOut = 0;
                    if (!cluster.equals(clusterout)) {
                        for (V outVert : clusterout.vertices()) {
                            shortestPath(originalGraph, vertice, outVert, ce, sum, zero, shortPath);
                            int dist = 0;
                            for (int i = 0, u = 1; u < shortPath.size(); i++, u++) {
                                dist += (int) originalGraph.edge(shortPath.get(i), shortPath.get(u)).getWeight();
                            }
                            sumDistOut += dist;
                        }
                        float avgDistOut = sumDistOut / clusterout.numVertices();
                        if (avgDistOut < lowAvgDistOut) {
                            lowAvgDistOut = avgDistOut;
                        }
                    }
                }
                sillouetteSum += calculateSillouette(lowAvgDistOut, avgDistIn);
                numSillouettes++;

            }
            sillouetteAverages.add(sillouetteSum / numSillouettes);
        }
        if (sillouetteAverages.stream().max(Float::compareTo).isPresent()) {
            return sillouetteAverages.stream().max(Float::compareTo).get();
        } else {
            return 0;
        }
    }

    private static float calculateSillouette(float lowAvgDistOut, float avgDistIn) {
        if (avgDistIn < lowAvgDistOut) {
            return (1 - (avgDistIn / lowAvgDistOut));
        } else if (avgDistIn > lowAvgDistOut) {
            return ((lowAvgDistOut / avgDistIn) - 1);
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

    /*
    public static <V, E> Graph<V, E> divideGraphN(Graph<V, E> g, Set<V> hubList, Comparator<E> ce,BinaryOperator<E> sum, E zero, int numClusters){
        Map<Edge<V, E>, Integer>  numShortPaths = new LinkedHashMap<>();
        for (Edge<V, E> e : g.edges()){
            numShortPaths.put(e, 0);
        }

        for (V vertice : g.vertices()){
            ArrayList<LinkedList<V>> verticePaths = new ArrayList<>();
            ArrayList<E> verticeDistance = new ArrayList<>();
            shortestPaths(g, vertice, ce, sum, zero, verticePaths, verticeDistance);
            for (LinkedList<V> list : verticePaths){
                numShortPaths(g, list, numShortPaths);
            }
        }
        List<Map.Entry<Edge<V, E>, Integer>> sortedMap = new ArrayList<>(numShortPaths.entrySet());
        sortedMap.sort(Map.Entry.comparingByValue(Integer::compareTo));
        sortedMap = sortedMap.reversed();


        for(Map.Entry<Edge<V, E>, Integer> entry : sortedMap){
            int numeroClustersAtuais = 0;
            g.removeEdge(entry.getKey().getVOrig(), entry.getKey().getVDest());

            LinkedList<V> visitados = new LinkedList<>();
            boolean clustersContainHubs = false;

            for(V vertice : g.vertices()){
                if (!visitados.contains(vertice)){
                    LinkedList<V> cluster = new LinkedList<>();
                    DepthFirstSearch(g, vertice, cluster);
                    for(V hub : hubList){
                        if (cluster.contains(hub)){
                            clustersContainHubs = true;
                            break;
                        }
                    }
                    if(!clustersContainHubs || (cluster.size() == g.numVertices())){
                        break;
                    } else {
                        visitados.addAll(cluster);
                        numeroClustersAtuais++;
                    }
                }
            }
            if (!clustersContainHubs)
                g.addEdge(entry.getKey().getVOrig(), entry.getKey().getVDest(), entry.getKey().getWeight());

            if (numeroClustersAtuais == numClusters) break;
        }

        return g;
    }
    */

    public static <V, E> Map<V, LinkedList<V>> divideGraphN(Graph<V, E> g, Set<V> hubList, Comparator<E> ce,BinaryOperator<E> sum, E zero, int numClusters){
        List<Map.Entry<Edge<V, E>, Integer>> sortedMap = sortMap(g, ce, sum, zero);

        for (Map.Entry<Edge<V, E>, Integer> entry : sortedMap){
            Graph<V, E> gBackup = g.clone();

            g.removeEdge(entry.getKey().getVOrig(), entry.getKey().getVDest());

            LinkedList<V> visitados = new LinkedList<>();

            boolean containsHub = true;

            int numClustersAtuais = 0;

            for(V vertice : g.vertices()){
                if(!visitados.contains(vertice)) {
                    LinkedList<V> cluster = new LinkedList<>();
                    DepthFirstSearch(g, vertice, cluster);
                    if(cluster.size() == g.numVertices()){
                        break;
                    } else {
                        if(Collections.disjoint(cluster, hubList)){
                            containsHub = false;
                            break;
                        } else {
                            numClustersAtuais++;
                            visitados.addAll(cluster);
                        }
                    }
                }
            }
            if(!containsHub){
                g = gBackup.clone();
            }
            if(numClustersAtuais == numClusters){
                break;
            }
        }

        return createClusterLists(g, hubList);
    }

    private static <V, E> Map<V, LinkedList<V>> createClusterLists(Graph<V, E> g, Set<V> hubList){
        Map<V, LinkedList<V>> returnMap = new HashMap<>();
        for (V hub : hubList){
            LinkedList<V> cluster = new LinkedList<>();
            DepthFirstSearch(g, hub, cluster);
            returnMap.put(hub, cluster);
        }
        return returnMap;
    }

    private static <V, E> List<Map.Entry<Edge<V, E>, Integer>> sortMap(Graph<V, E> g, Comparator<E> ce,BinaryOperator<E> sum, E zero){
        Map<Edge<V, E>, Integer>  numShortPaths = new LinkedHashMap<>();
        for (Edge<V, E> e : g.edges()){
            numShortPaths.put(e, 0);
        }

        for (V vertice : g.vertices()){
            ArrayList<LinkedList<V>> verticePaths = new ArrayList<>();
            ArrayList<E> verticeDistance = new ArrayList<>();
            shortestPaths(g, vertice, ce, sum, zero, verticePaths, verticeDistance);
            for (LinkedList<V> list : verticePaths){
                numShortPaths(g, list, numShortPaths);
            }
        }
        List<Map.Entry<Edge<V, E>, Integer>> sortedMap = new ArrayList<>(numShortPaths.entrySet());
        sortedMap.sort(Map.Entry.comparingByValue(Integer::compareTo));
        sortedMap = sortedMap.reversed();

        return sortedMap;
    }

    private static <V, E> void numShortPaths(Graph<V, E> g, LinkedList<V> listVertices, Map<Edge<V, E>, Integer>  numShortPaths){
        for (int i = 0; i < listVertices.size() - 1; i++){
            V vOrigin = listVertices.get(i);
            V vDest = listVertices.get(i + 1);
            int currentNum = numShortPaths.get(g.edge(vOrigin, vDest));
            numShortPaths.put(g.edge(vOrigin, vDest), currentNum + 1);
        }
    }

    public static <V, E> Pair<Integer, Map<V, Map<V, Integer>>> fordFulkerson(Graph<V, E> g, V source, V sink) {
        int maxFlow = 0;

        Map<V, Map<V, Integer>> residualGraph = new HashMap<>();
        for (V vertex : g.vertices()) {
            residualGraph.put(vertex, new HashMap<>());
            for (V adjVertex : g.adjVertices(vertex)) {
                Edge<V, E> edge = g.edge(vertex, adjVertex);
                int weight = (Integer) edge.getWeight();
                residualGraph.get(vertex).put(adjVertex, weight);
                if (!residualGraph.containsKey(adjVertex)) {
                    residualGraph.put(adjVertex, new HashMap<>());
                }
                residualGraph.get(adjVertex).put(vertex, weight);
            }
        }

        Map<V, V> parent = new HashMap<>();

        while (bfs(residualGraph, source, sink, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            List<V> path = new ArrayList<>();
            for (V v = sink; v != source; v = parent.get(v)) {
                V u = parent.get(v);
                pathFlow = Math.min(pathFlow, residualGraph.get(u).get(v));
                path.add(0, v);
            }
            path.add(0, source);

            for (V v = sink; v != source; v = parent.get(v)) {
                V u = parent.get(v);
                int residualCapacity = residualGraph.get(u).get(v);
                int reverseCapacity = residualGraph.get(v).get(u);
                if (pathFlow <= residualCapacity) {
                    residualGraph.get(u).put(v, residualCapacity - pathFlow);
                    residualGraph.get(v).put(u, reverseCapacity + pathFlow);
                }
            }

            maxFlow += pathFlow;
        }

        return new Pair<>(maxFlow, residualGraph);
    }

    private static <V> boolean bfs(Map<V, Map<V, Integer>> residualGraph, V source, V sink, Map<V, V> parent) {
        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();
        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            V vertex = queue.poll();
            for (Map.Entry<V, Integer> entry : residualGraph.get(vertex).entrySet()) {
                V adjVertex = entry.getKey();
                Integer capacity = entry.getValue();
                if (!visited.contains(adjVertex) && capacity > 0) {
                    queue.add(adjVertex);
                    visited.add(adjVertex);
                    parent.put(adjVertex, vertex);
                    if (adjVertex.equals(sink)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //Give me an algorithm to find the paths between N hubs from a given vertex Origin that is it the vertex Final
    public static <V, E> List<List<V>> allPathsN(Graph<V, E> g, V vOrig, V vDest, int n) {
        List<List<V>> paths = new ArrayList<>();
        List<V> hubs = new ArrayList<>();
        for (V v : g.vertices()) {
            if (((Location) v).isHub() && !v.equals(vOrig)) {
                hubs.add(v);
            }
        }

        depthFirstSearchWithHubs(g, vOrig, vDest, n, hubs, new ArrayList<>(Collections.singletonList(vOrig)), paths,
                new HashSet<>());
        return paths;
    }

    //make the search where the vInitial needs to be also the vDest and i dont want to repeat the hubs and in the nHubs necessary
    public static <V, E> void depthFirstSearchWithHubs(Graph<V, E> g, V v, V vDest, int n, List<V> hubs,
            List<V> path, List<List<V>> paths, Set<V> visited) {
        //int numHubsCountingWithOrigin = n + 1;
        if (path.size() > n) {
            return;
        }

        if (v.equals(vDest)) {
            paths.add(new ArrayList<>(path));
        }

        for(V vAdj : g.adjVertices(v)) {
            if (!visited.contains(vAdj)) {// Skip if vertex has been visited
                visited.add(vAdj);
                if (hubs.contains(vAdj)) {
                    path.add(vAdj);
                    depthFirstSearchWithHubs(g, vAdj, vDest, n, hubs, path, paths, new HashSet<>(visited)); // Pass a copy of visited set
                    path.remove(path.size() - 1);
                } else {
                    depthFirstSearchWithHubs(g, vAdj, vDest, n, hubs, path, paths, new HashSet<>(visited)); // Pass a copy of visited set
                }
            }
        }
    }


}