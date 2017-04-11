package housing;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.AverageRoomRecord;

public class AverageRoomPercentileCombiner
        extends Reducer<Text, AverageRoomRecord, Text, AverageRoomRecord> {
    @Override
    protected void reduce(Text key, Iterable<AverageRoomRecord> values, Context context)
            throws IOException, InterruptedException {
        AverageRoomRecord averageRoomRecord = new AverageRoomRecord();

        for (AverageRoomRecord val : values) {
            combineCounts(averageRoomRecord, val.getRoomCounts());
        }

        context.write(key, averageRoomRecord);
    }

    private void combineCounts(AverageRoomRecord averageRoomRecord, long[] roomCountToUpdate) {
        long[] roomCounts = averageRoomRecord.getRoomCounts();
        for (int i = 0; i < AverageRoomRecord.NUM_ROOMS; i++) {
            roomCounts[i] += roomCountToUpdate[i];
        }
        averageRoomRecord.setRoomCounts(roomCounts);
    }
}