package cs455.overlay.graph;

public class Edge {

    private int weight;

    private int vertex;

    public Edge(int weight, int vertex) {
        this.weight = weight;
        this.vertex = vertex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Edge.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        Edge other = (Edge) obj;
        if (this.vertex != other.vertex) {
            return false;
        }
        return true;
    }

    public int getWeight() {
        return weight;
    }

    public int getVertex() {
        return vertex;
    }
}
