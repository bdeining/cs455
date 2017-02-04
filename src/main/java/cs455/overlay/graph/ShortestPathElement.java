package cs455.overlay.graph;

public class ShortestPathElement {

    private int[] dist;

    private int[] pred;

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
