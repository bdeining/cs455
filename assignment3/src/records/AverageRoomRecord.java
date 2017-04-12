package records;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class AverageRoomRecord implements Writable {

    public static final int NUM_ROOMS = 9;

    private long[] roomCounts;

    public AverageRoomRecord() {
        roomCounts = new long[NUM_ROOMS];
    }

    public long[] getRoomCounts() {
        return roomCounts;
    }

    public void setRoomCounts(long[] roomCounts) {
        this.roomCounts = roomCounts;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        for (int i = 0; i < NUM_ROOMS; i++) {
            dataOutput.writeLong(roomCounts[i]);
        }
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        for (int i = 0; i < NUM_ROOMS; i++) {
            roomCounts[i] = dataInput.readLong();
        }
    }
}
