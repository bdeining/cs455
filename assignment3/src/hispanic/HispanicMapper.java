package hispanic;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import record.HispanicRecord;
import util.RecordParsingUtils;

public class HispanicMapper extends Mapper<LongWritable, Text, Text, HispanicRecord> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String unparsedText = value.toString();
        String summaryLevel = RecordParsingUtils.getSummaryLevel(unparsedText);
        if (summaryLevel.equals("100")) {


            String state = RecordParsingUtils.getState(unparsedText);
            Long logicalRecordPartNumber = RecordParsingUtils.getLogicalRecordPartNumber(
                    unparsedText);
            Long totalNumberOfPartsInRecord = RecordParsingUtils.getTotalNumberOfPartsInRecord(
                    unparsedText);

            HispanicRecord hispanicRecord = new HispanicRecord();

            if (!logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                hispanicRecord.setHispanic0to18(getHispanicBelow18(unparsedText));
                hispanicRecord.setHispanic19to29(getHispanic19to29(unparsedText));
                hispanicRecord.setHispanic30to39(getHispanic30to39(unparsedText));
                hispanicRecord.setLogicalRecordPartNumber(logicalRecordPartNumber);
                hispanicRecord.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
                context.write(new Text(state), hispanicRecord);
            }
        }
    }
    /*
    (a).Percentage of people below 18 years (inclusive) old.
    /* 3864 - 3972, 4144 - 4252 */
    public Long getHispanicBelow18(String unparsedText) {
        Long hispanicBelow18 = 0L;

        /* Male */
        for (int i = 3864; i < 3973; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanicBelow18 += reading;
        }

        /* Female */
        for (int i = 4144; i < 4253; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanicBelow18 += reading;
        }

        return hispanicBelow18;
    }

    /* 3981 - 4017, 4261 - 4298
    *  (b).Percentage of people between 19 (inclusive) and 29 (inclusive) years old.
    * */
    public Long getHispanic19to29(String unparsedText) {
        Long hispanic19to29 = 0L;

        /* Male */
        for (int i = 3981; i < 4018; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanic19to29 += reading;
        }

        /* Female */
        for (int i = 4261; i < 4298; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanic19to29 += reading;
        }

        return hispanic19to29;
    }

    /*
            4026 - 4035
     *     (c). Percentage of people between 30 (inclusive) and 39 (inclusive) years old.
     */

    public Long getHispanic30to39(String unparsedText) {
        Long hispanic30to39 = 0L;

        /* Male */
        for (int i = 4026; i < 4036; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanic30to39 += reading;
        }

        /* Female */
        for (int i = 4306; i < 4316; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanic30to39 += reading;
        }

        return hispanic30to39;
    }

}
