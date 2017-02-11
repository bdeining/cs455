package cs455.overlay.graph;

import java.util.ArrayList;
import java.util.List;

public class ShortestPath {

    public static ShortestPathElement dijkstra(Graph graph, int source) {
        int size = graph.getAdjacencyList()
                .size();

        final int[] dist = new int[size];
        final int[] pred = new int[size];
        final boolean[] visited = new boolean[size];

        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[source] = 0;

        for (int i = 0; i < dist.length; i++) {
            final int next = minVertex(dist, visited);
            visited[next] = true;

            final List<Edge> edges = graph.getAdjacencyList()
                    .get(next);
            for (int j = 0; j < edges.size(); j++) {
                final int vertex = edges.get(j).getVertex();
                final int distance = dist[next] + graph.getWeight(next, vertex);
                if (dist[vertex] > distance) {
                    dist[vertex] = distance;
                    pred[vertex] = next;
                }
            }
        }
        return new ShortestPathElement(dist, pred);
    }

    private static int minVertex(int[] dist, boolean[] visited) {
        int minimumValue = Integer.MAX_VALUE;
        int vertex = -1;
        for (int i = 0; i < dist.length; i++) {
            if (!visited[i] && dist[i] < minimumValue) {
                vertex = i;
                minimumValue = dist[i];
            }
        }
        return vertex;
    }

    public static List<Integer> getShortestPathIntegerList(int[] pred, int source, int destination) {
        final List<Integer> path = new ArrayList<>();
        int vertex = destination;
        while (vertex != source) {
            path.add(0, vertex);
            vertex = pred[vertex];
        }
        path.add(0, source);
        return path;
    }

    public static List<String> getShortestPathStringList(Graph graph, int[] pred, int source,
            int destination) {
        final List<String> path = new ArrayList<>();
        int vertex = destination;
        while (vertex != source) {
            path.add(graph.getMappedVertex(vertex) + "--" + graph.getEdgeWeight(vertex, pred[vertex]));
            vertex = pred[vertex];
        }
        path.add(graph.getMappedVertex(source));
        return path;
    }
}
