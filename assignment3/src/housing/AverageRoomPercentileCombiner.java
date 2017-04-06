package housing;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.AverageRoomRecord;
import record.HousingMedianRecord;

public class AverageRoomPercentileCombiner
        extends Reducer<Text, AverageRoomRecord, Text, AverageRoomRecord> {
    @Override
    protected void reduce(Text key, Iterable<AverageRoomRecord> values, Context context)
            throws IOException, InterruptedException {
        AverageRoomRecord averageRoomRecord = new AverageRoomRecord();
        long totalNumberOfPartsInRecord = 0;
        long logicalRecodPartNumber = 0;

        for (AverageRoomRecord val : values) {
            combineCounts(averageRoomRecord, val.getRoomCounts());
            totalNumberOfPartsInRecord = val.getTotalNumberOfPartsInRecord();
            logicalRecodPartNumber = val.getLogicalRecordPartNumber();
        }

        averageRoomRecord.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
        averageRoomRecord.setLogicalRecordPartNumber(logicalRecodPartNumber);
        context.write(key, averageRoomRecord);
    }

    private void combineCounts(AverageRoomRecord averageRoomRecord, long[] roomCountToUpdate) {
        long[] roomCounts = averageRoomRecord.getRoomCounts();
        for (int i =0; i<9; i++) {
            roomCounts[i] += roomCountToUpdate[i];
        }

        averageRoomRecord.setRoomCounts(roomCounts);
    }
}