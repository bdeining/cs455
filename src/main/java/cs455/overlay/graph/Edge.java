package cs455.overlay.graph;

public class Edge {

    private int weight;

    private int vertex;

    public Edge(int weight, int vertex) {
        this.weight = weight;
        this.vertex = vertex;
    }

    public int getWeight() {
        return weight;
    }

    public int getVertex() {
        return vertex;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
