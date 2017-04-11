package housing;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import record.AverageRoomRecord;
import util.RecordParsingUtils;

public class AverageRoomPercentileMapper
        extends Mapper<LongWritable, Text, Text, AverageRoomRecord> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String unparsedText = value.toString();
        String summaryLevel = RecordParsingUtils.getSummaryLevel(unparsedText);
        if (summaryLevel.equals(RecordParsingUtils.SUMMARY_LEVEL)) {

            String state = RecordParsingUtils.getState(unparsedText);
            Long logicalRecordPartNumber = RecordParsingUtils.getLogicalRecordPartNumber(
                    unparsedText);
            Long totalNumberOfPartsInRecord = RecordParsingUtils.getTotalNumberOfPartsInRecord(
                    unparsedText);

            AverageRoomRecord averageRoomRecord = new AverageRoomRecord();

            if (logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                averageRoomRecord.setRoomCounts(getRoomCounts(unparsedText));
                context.write(new Text(state), averageRoomRecord);
            }
        }
    }

    private long[] getRoomCounts(String unparsedText) {
        long[] roomSizes = new long[AverageRoomRecord.NUM_ROOMS];

        int count = 0;
        for (int i = 2388; i < 2461; i += 9) {
            long number = Long.parseLong(unparsedText.substring(i, i + 9));
            roomSizes[count] = number;
            count++;
        }

        return roomSizes;
    }
}
