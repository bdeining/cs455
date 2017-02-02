package cs455.overlay.graph;

public class ShortestPathElement {

    int[] dist;
    int[] pred;

    public ShortestPathElement(int[] dist, int[] pred) {
        this.dist = dist;
        this.pred = pred;
    }

    public int[] getDist() {
        return dist;
    }

    public int[] getPred() {
        return pred;
    }
}
