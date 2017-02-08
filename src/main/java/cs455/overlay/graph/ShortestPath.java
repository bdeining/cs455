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

            final List<Edge> n = graph.getAdjacencyList()
                    .get(next);
            for (int j = 0; j < n.size(); j++) {
                final int v = n.get(j)
                        .getVertex();
                final int d = dist[next] + graph.getWeight(next, v);
                if (dist[v] > d) {
                    dist[v] = d;
                    pred[v] = next;
                }
            }
        }
        return new ShortestPathElement(dist, pred);
    }

    private static int minVertex(int[] dist, boolean[] visited) {
        int x = Integer.MAX_VALUE;
        int y = -1;
        for (int i = 0; i < dist.length; i++) {
            if (!visited[i] && dist[i] < x) {
                y = i;
                x = dist[i];
            }
        }
        return y;
    }

    public static List<Integer> printPath(int[] pred, int source, int edge) {
        final List<Integer> path = new ArrayList<>();
        int x = edge;
        while (x != source) {
            path.add(0, x);
            x = pred[x];
        }
        path.add(0, source);
        System.out.println(path);
        return path;
    }

}
