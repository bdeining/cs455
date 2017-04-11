package housing;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import record.RentMedianHouseValueRecord;
import util.RecordParsingUtils;

public class RentMedianHouseValueMapper
        extends Mapper<LongWritable, Text, Text, RentMedianHouseValueRecord> {

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

            RentMedianHouseValueRecord rentMedianHouseValueRecord =
                    new RentMedianHouseValueRecord();

            if (!logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                rentMedianHouseValueRecord.setUnmarriedPopulation(getUnmarriedPopulation(
                        unparsedText));
                rentMedianHouseValueRecord.setRentTenure(getRenterOccupied(unparsedText));
                context.write(new Text(state), rentMedianHouseValueRecord);
            }
        }
    }

    private Long getUnmarriedPopulation(String unparsedText) {
        long totalUnmarried = 0;
        /* Male */
        totalUnmarried += Long.parseLong(unparsedText.substring(4422, 4431));
        totalUnmarried += Long.parseLong(unparsedText.substring(4440, 4449));
        totalUnmarried += Long.parseLong(unparsedText.substring(4449, 4458));
        /* Female */
        totalUnmarried += Long.parseLong(unparsedText.substring(4467, 4476));
        totalUnmarried += Long.parseLong(unparsedText.substring(4476, 4485));
        totalUnmarried += Long.parseLong(unparsedText.substring(4485, 4494));
        return totalUnmarried;
    }

    private Long getRenterOccupied(String unparsedText) {
        Long renterOccupied = Long.parseLong(unparsedText.substring(1812, 1821));
        return renterOccupied;
    }
}
