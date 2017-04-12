package mappers;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import records.TenureRecord;
import util.RecordParsingUtils;

public class TenureMapper extends Mapper<LongWritable, Text, Text, TenureRecord> {

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

            TenureRecord tenureRecord = new TenureRecord();

            if (logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                tenureRecord.setRented(getRenterOccupied(unparsedText));
                tenureRecord.setOwned(getOwnerOccupied(unparsedText));
                context.write(new Text(state), tenureRecord);
            }
        }
    }

    private Long getOwnerOccupied(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(1803, 1812, unparsedText);
    }

    private Long getRenterOccupied(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(1812, 1821, unparsedText);
    }
}
