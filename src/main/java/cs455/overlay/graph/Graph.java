package cs455.overlay.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Graph {

    private final ArrayList<LinkedList<Edge>> adjacencyList;

    private Map<String, Integer> hostToVertexMap;

    private Random random = new Random();

    private Map<Integer, String> vertexToHostMap;

    private int numberOfConnections;

    public Graph(List<String> vertices, int numberOfConnections) {
        hostToVertexMap = new HashMap<>();
        vertexToHostMap = new HashMap<>();
        adjacencyList = new ArrayList<>();
        this.numberOfConnections = numberOfConnections;

        int counter = 0;
        for (String string : vertices) {
            hostToVertexMap.put(string, counter);
            vertexToHostMap.put(counter, string);
            adjacencyList.add(new LinkedList<>());
            counter++;
        }
    }

    public Integer getMappedVertex(String x) {
        return hostToVertexMap.get(x);
    }

    public String getMappedVertex(Integer x) {
        return vertexToHostMap.get(x);
    }

    public void generateConnectedGraph() {
        generateLinearRing();
        randomlyConnectVertices();
    }

    /**
     * Generate a linear ring in the graph to ensure connectivity
     */
    private void generateLinearRing() {
        adjacencyList.get(0)
                .add(new Edge(0, adjacencyList.size() - 1));
        adjacencyList.get(adjacencyList.size() - 1)
                .add(new Edge(0, 0));
        for (int i = 0; i < adjacencyList.size() - 1; i++) {
            adjacencyList.get(i)
                    .add(new Edge(0, i + 1));
            adjacencyList.get(i + 1)
                    .add(new Edge(0, i));
        }
    }

    /**
     * Randomly assign vertices until the number of connections specified is met
     */
    private void randomlyConnectVertices() {
        for (int i = 0; i < adjacencyList.size(); i++) {
            while (!isConnectionLimitReached(i)) {
                Edge vertex = getRandomVertex(i);
                if (!adjacencyList.get(i)
                        .contains(vertex)) {
                    adjacencyList.get(i)
                            .add(vertex);
                }
            }
        }
    }

    private boolean isConnectionLimitReached(int vertex) {
        if (adjacencyList.get(vertex)
                .size() != numberOfConnections) {
            return false;
        }
        return true;
    }

    private Edge getRandomVertex(int vertex) {
        int min = 0;
        int max = adjacencyList.size() - 1;
        int result = random.nextInt((max - min) + 1) + min;

        while (vertex == result) {
            result = random.nextInt((max - min) + 1) + min;
        }
        return new Edge(0, result);
    }

    private int getRandomEdgeWeight() {
        return random.nextInt(11);
    }

    public void randomlyAssignEdgeWeights() {
        for (int i = 0; i < adjacencyList.size(); i++) {
            List<Edge> edges = adjacencyList.get(i);
            for (int c = 0; c < edges.size(); c++) {
                Edge edge = edges.get(c);
                edge.setWeight(getRandomEdgeWeight());
            }
        }
    }

    /**
     * BFS to determine all nodes are visited
     *
     * @return true if the graph is connected
     */
    public boolean isConnected() {
        int[] visited = new int[adjacencyList.size()];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = 0;
        }

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);

        while (!stack.isEmpty()) {
            Integer vertex = stack.pop();
            List<Edge> edges = adjacencyList.get(vertex);
            if (visited[vertex] == 0) {
                visited[vertex] = 1;
                for (Edge edge : edges) {
                    stack.push(edge.getVertex());
                }
            }
        }

        for (int i = 0; i < visited.length; i++) {
            if (visited[i] == 0) {
                return false;
            }
        }
        return true;
    }

    public String generateLinkWeightBody() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<adjacencyList.size(); i++) {
            String source = getMappedVertex(i);
            List<Edge> edges = adjacencyList.get(i);
            for(int c=0; c< edges.size(); c++) {
                Edge edge = edges.get(c);
                String destination = getMappedVertex(edge.getVertex());
                stringBuilder.append(source);
                stringBuilder.append(" ");
                stringBuilder.append(destination);
                stringBuilder.append(" ");
                stringBuilder.append(edge.getWeight());
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }
}
