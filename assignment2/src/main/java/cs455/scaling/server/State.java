package cs455.scaling.server;

public class State {

    private String operation;

    private String hashCode;

    public State(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return hashCode;
    }
}
