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

    private final List<LinkedList<Edge>> adjacencyList;

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

    public List<LinkedList<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public int getWeight(int next, int v) {
        List<Edge> edges = adjacencyList.get(next);
        for(Edge edge : edges) {
            if(edge.getVertex() == v) {
                return edge.getWeight();
            }
        }
        return -1;
    }

    public Graph(List<String> messageNodeList) {
        hostToVertexMap = new HashMap<>();
        vertexToHostMap = new HashMap<>();
        adjacencyList = new ArrayList<>();
        int counter = 0;
        for (String string : messageNodeList) {
            String[] strings = string.split(" ");
            String source = strings[0];
            if (!hostToVertexMap.containsKey(source)) {
                hostToVertexMap.put(source, counter);
                vertexToHostMap.put(counter, source);
                adjacencyList.add(new LinkedList<>());
                counter++;
            }
        }

        for (String string : messageNodeList) {
            String[] strings = string.split(" ");
            if (strings.length == 3) {
                int edgeWeight = Integer.parseInt(strings[2]);
                addVertex(getMappedVertex(strings[0]), getMappedVertex(strings[1]), edgeWeight);
            }
        }
    }

    public void addVertex(int source, int destination, int edgeWeight) {
        adjacencyList.get(source)
                .add(new Edge(edgeWeight, destination));
    }

    public boolean isGraphEqual(Graph graph) {
        if (graph.adjacencyList.size() != adjacencyList.size()) {
            return false;
        }

        for (int i = 0; i < adjacencyList.size(); i++) {
            List<Edge> edges1 = adjacencyList.get(i);
            List<Edge> edges2 = graph.adjacencyList.get(i);
            if (edges1.size() != edges2.size()) {
                return false;
            }

            for (Edge edge : edges1) {
                if (!edges2.contains(edge)) {
                    return false;
                }
            }

        }
        return true;
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
        int randomWeight = getRandomEdgeWeight();
        adjacencyList.get(0)
                .add(new Edge(randomWeight, adjacencyList.size() - 1));
        adjacencyList.get(adjacencyList.size() - 1)
                .add(new Edge(randomWeight, 0));
        for (int i = 0; i < adjacencyList.size() - 1; i++) {
            randomWeight = getRandomEdgeWeight();
            adjacencyList.get(i)
                    .add(new Edge(randomWeight, i + 1));
            adjacencyList.get(i + 1)
                    .add(new Edge(randomWeight, i));
        }
    }

    /**
     * Randomly assign vertices until the number of connections specified is met
     */
    private void randomlyConnectVertices() {
        for (int i = 0; i < adjacencyList.size(); i++) {
            while (!isConnectionLimitReached(i)) {
                Edge vertex = getRandomVertex(i);
                Edge backVertex = new Edge(vertex.getWeight(), i);
                if (!adjacencyList.get(i)
                        .contains(vertex)) {
                    if (!adjacencyList.get(vertex.getVertex())
                            .contains(backVertex)) {
                        adjacencyList.get(i)
                                .add(vertex);
                        adjacencyList.get(vertex.getVertex())
                                .add(backVertex);
                    }
                }
            }
        }
    }

    private boolean isConnectionLimitReached(int vertex) {
        if (adjacencyList.get(vertex)
                .size() < numberOfConnections) {
            return false;
        }
        return true;
    }

    public String getRandomHost(String source) {
        int sourceInt = getMappedVertex(source);
        return getMappedVertex(getRandomVertex(sourceInt).getVertex());
    }

    private Edge getRandomVertex(int vertex) {
        int min = 0;
        int max = adjacencyList.size() - 1;
        int result = random.nextInt((max - min) + 1) + min;

        while (vertex == result) {
            result = random.nextInt((max - min) + 1) + min;
        }
        return new Edge(getRandomEdgeWeight(), result);
    }

    private int getRandomEdgeWeight() {
        return random.nextInt(10) + 1;
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

    public List<String> generateMessageNodeList(String source) {
        List<String> messageNodeList = new ArrayList<>();
        List<String> stringList = generateMessageNodeFullList();
        for (String string : stringList) {
            if (string.startsWith(source)) {
                messageNodeList.add(string);
            }
        }
        return messageNodeList;
    }

    public List<String> getShortestPath(String source, String destination) {
        int destinationInt = getMappedVertex(destination);
        int sourceInt = getMappedVertex(source);
        ShortestPathElement shortestPathElement = ShortestPath.dijkstra(this, sourceInt);
        List<Integer> dest = ShortestPath.printPath(shortestPathElement.getPred(),
                sourceInt,
                destinationInt);
        List<String> paths = new ArrayList<>();
        for(Integer integer : dest) {
            paths.add(getMappedVertex(integer));
        }
        return paths;
    }

    public List<String> generateMessageNodeFullList() {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < adjacencyList.size(); i++) {
            List<Edge> edges = adjacencyList.get(i);
            for (int c = 0; c < edges.size(); c++) {
                Edge edge2 = edges.get(c);
                String edgeA = getMappedVertex(i) + " " + getMappedVertex(edge2.getVertex());
                String edgeB = getMappedVertex(edge2.getVertex()) + " " + getMappedVertex(i);
                if (!stringList.contains(edgeA) && !stringList.contains(edgeB)) {
                    stringList.add(edgeA);
                }
            }
        }

        return stringList;
    }

    public List<String> generateLinkWeightList() {
        List<String> linkWeights = new ArrayList<>();
        for (int i = 0; i < adjacencyList.size(); i++) {
            String source = getMappedVertex(i);
            List<Edge> edges = adjacencyList.get(i);
            for (int c = 0; c < edges.size(); c++) {
                Edge edge = edges.get(c);
                String destination = getMappedVertex(edge.getVertex());
                int weight = edge.getWeight();
                String link = source + " " + destination + " " + weight;
                linkWeights.add(link);
            }
        }
        return linkWeights;
    }

    public String generateLinkWeightBody() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < adjacencyList.size(); i++) {
            String source = getMappedVertex(i);
            List<Edge> edges = adjacencyList.get(i);
            for (int c = 0; c < edges.size(); c++) {
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
