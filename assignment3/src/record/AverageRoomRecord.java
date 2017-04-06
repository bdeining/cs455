package record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AverageRoomRecord extends Record {

    public static final int NUM_ROOMS = 9;

    private long[] roomCounts;

    public AverageRoomRecord() {
        roomCounts = new long[NUM_ROOMS];
    }

    public void setRoomCounts(long[] roomCounts) {
        this.roomCounts = roomCounts;
    }

    public long[] getRoomCounts() {
        return roomCounts;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        super.write(dataOutput);
        for (int i = 0; i < NUM_ROOMS; i++) {
            dataOutput.writeLong(roomCounts[i]);
        }
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        super.readFields(dataInput);
        for (int i = 0; i < NUM_ROOMS; i++) {
            roomCounts[i] = dataInput.readLong();
        }
    }
}
