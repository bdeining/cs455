package cs455.scaling.server;

import java.nio.ByteBuffer;

public class State {

    private String operation;

    private ByteBuffer bytes;

    public State(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public ByteBuffer getBytes() {
        return bytes;
    }

    public void setBytes(ByteBuffer bytes) {
        this.bytes = bytes;
    }

}
