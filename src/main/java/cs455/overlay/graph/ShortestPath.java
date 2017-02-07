package cs455.overlay.graph;

import java.util.ArrayList;
import java.util.List;

public class ShortestPath {

    public static ShortestPathElement dijkstra(Graph G, int s) {
        int size = G.getAdjacencyList()
                .size();

        final int[] dist = new int[size];
        final int[] pred = new int[size];
        final boolean[] visited = new boolean[size];

        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[s] = 0;

        for (int i = 0; i < dist.length; i++) {
            final int next = minVertex(dist, visited);
            visited[next] = true;

            final List<Edge> n = G.getAdjacencyList()
                    .get(next);
            for (int j = 0; j < n.size(); j++) {
                final int v = n.get(j)
                        .getVertex();
                final int d = dist[next] + G.getWeight(next, v);
                if (dist[v] > d) {
                    dist[v] = d;
                    pred[v] = next;
                }
            }
        }
        return new ShortestPathElement(dist, pred);
    }

    private static int minVertex(int[] dist, boolean[] v) {
        int x = Integer.MAX_VALUE;
        int y = -1;
        for (int i = 0; i < dist.length; i++) {
            if (!v[i] && dist[i] < x) {
                y = i;
                x = dist[i];
            }
        }
        return y;
    }

    public static List<Integer> printPath(int[] pred, int s, int e) {
        final List<Integer> path = new ArrayList<>();
        int x = e;
        while (x != s) {
            path.add(0, x);
            x = pred[x];
        }
        path.add(0, s);
        System.out.println(path);
        return path;
    }

}
