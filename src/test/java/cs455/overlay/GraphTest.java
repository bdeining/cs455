package cs455.overlay;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cs455.overlay.graph.Edge;
import cs455.overlay.graph.Graph;
import cs455.overlay.graph.ShortestPath;
import cs455.overlay.graph.ShortestPathElement;

public class GraphTest {
    private Graph graph;

    @Before
    public void setUp() {
        graph = new Graph(generateNodes(5), 4);
    }

    @Test
    public void testInit() {
        assertNodes(5);
    }

    @Test
    public void testLinkWeightBody() {
        graph.generateConnectedGraph();
        graph.generateLinkWeightBody();
        System.out.println(graph.generateLinkWeightBody());
    }

    @Test
    public void testIsNotConnected() {
        assertThat(graph.isConnected(), is(false));
    }

    @Test
    public void testIsConnected() {
        graph.generateConnectedGraph();
        assertThat(graph.isConnected(), is(true));
    }

    @Test
    public void testIsConnected10Nodes() {
        graph = new Graph(generateNodes(10), 4);
        graph.generateConnectedGraph();
        assertThat(graph.isConnected(), is(true));
        assertNodes(10);
    }

    @Test
    public void testIsConnected100Nodes() {
        graph = new Graph(generateNodes(100), 4);
        graph.generateConnectedGraph();
        assertThat(graph.isConnected(), is(true));
        assertNodes(100);
    }

    @Test
    public void testIsConnected100Nodes10Connections() {
        graph = new Graph(generateNodes(100), 10);
        graph.generateConnectedGraph();
        assertThat(graph.isConnected(), is(true));
        assertNodes(100);
    }

    @Test
    public void testEquals() {
        Edge edge1 = new Edge(1, 2);
        Edge edge2 = new Edge(1, 2);
        Edge edge3 = new Edge(1, 3);
        assertThat(edge1.equals(edge2), is(true));
        assertThat(edge1.equals(edge3), is(false));
        assertThat(edge2.equals(edge3), is(false));
    }

    @Test
    public void testRandomEdgeWeight() {
        graph = new Graph(generateNodes(10), 4);
        graph.generateConnectedGraph();
        assertThat(graph.isConnected(), is(true));
    }

    @Test
    public void testMessageNodeListWeightListCtor() {
        graph = new Graph(generateNodes(10), 4);
        graph.generateConnectedGraph();

        String body = graph.generateLinkWeightBody();
        List<String> stringList = Arrays.asList(body.split("\n"));
        Graph graph2 = new Graph(stringList);
        assertThat(graph.isGraphEqual(graph2), is(true));
        assertThat(graph2.isConnected(), is(true));
    }

    @Test
    public void testMessageNodeListWeight5ListCtor() {
        graph = new Graph(generateNodes(5), 4);
        graph.generateConnectedGraph();

        String body = graph.generateLinkWeightBody();
        List<String> stringList = Arrays.asList(body.split("\n"));
        Graph graph2 = new Graph(stringList);
        assertThat(graph.isGraphEqual(graph2), is(true));
        assertThat(graph2.isConnected(), is(true));
    }

    @Test
    public void testShortestPath() {

        graph.generateConnectedGraph();
        String stringList = graph.generateLinkWeightBody();
        System.out.println(stringList);
        ShortestPathElement shortestPathElement = ShortestPath.dijkstra(graph, 0);
        int[] dist = shortestPathElement.getDist();
        int[] pred = shortestPathElement.getPred();


        for(int i=0; i<dist.length; i++) {
            System.out.println(i + " " + dist[i]);
        }

        List<Integer> integers = ShortestPath.getShortestPath(pred, 0, 1);
        System.out.println(integers);
        integers = ShortestPath.getShortestPath(pred, 0, 2);
        System.out.println(integers);
        integers = ShortestPath.getShortestPath(pred, 0, 3);
        System.out.println(integers);
        integers = ShortestPath.getShortestPath(pred, 0, 4);
        System.out.println(integers);

    }

    @Test
    public void testGetShortestPath2() {
        graph.generateConnectedGraph();
        System.out.println(graph.generateLinkWeightBody());


        for(int i = 0; i<graph.getAdjacencyList().size(); i++) {
            ShortestPathElement shortestPathElement = ShortestPath.dijkstra(graph, i);
            for (int c = 0; c < graph.getAdjacencyList().size(); c++) {
                if (i != c) {
                    List<String> path = ShortestPath.getShortestPath(graph,
                            shortestPathElement.getPred(),
                            i,
                            c);
                    System.out.println(String.join("--", path));
                }
            }
        }
    }

    @Test
    public void testFullMessageNodeList() {
        graph = new Graph(generateNodes(10), 4);
        graph.generateConnectedGraph();
        graph.generateMessageNodeFullList();

        List<String> string = graph.generateMessageNodeList("127.0.0.0");
        System.out.println(string.toString());
    }

    private List<String> generateNodes(int nodes) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < nodes; i++) {
            result.add("127.0.0." + i);
        }
        return result;
    }

    private void assertNodes(int nodes) {
        for (int i = 0; i < nodes; i++) {
            assertThat(graph.getMappedVertex(i), is("127.0.0." + i));
            assertThat(graph.getMappedVertex("127.0.0." + i), is(i));
        }
    }

}
