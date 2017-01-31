package cs455.overlay;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cs455.overlay.graph.Graph;

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
    public void testRandomEdgeWeight() {
        graph = new Graph(generateNodes(10), 4);
        graph.generateConnectedGraph();
        graph.randomlyAssignEdgeWeights();
        assertThat(graph.isConnected(), is(true));
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
