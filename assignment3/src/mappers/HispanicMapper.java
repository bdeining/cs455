package mappers;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import records.HispanicRecord;
import util.RecordParsingUtils;

public class HispanicMapper extends Mapper<LongWritable, Text, Text, HispanicRecord> {

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

            HispanicRecord hispanicRecord = new HispanicRecord();

            if (!logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                hispanicRecord.setMaleHispanic0to18(getMaleHispanicBelow18(unparsedText));
                hispanicRecord.setFemaleHispanic0to18(getFemaleHispanicBelow18(unparsedText));

                hispanicRecord.setMaleHispanic19to29(getMaleHispanic19to29(unparsedText));
                hispanicRecord.setFemaleHispanic19to29(getFemaleHispanic19to29(unparsedText));

                hispanicRecord.setMaleHispanic30to39(getMaleHispanic30to39(unparsedText));
                hispanicRecord.setFemaleHispanic30to39(getFemaleHispanic30to39(unparsedText));

                hispanicRecord.setTotalMaleHispanicPopulation(getTotalMalePopulation(unparsedText));
                hispanicRecord.setTotalFemaleHispanicPopulation(getTotalFemalePopulation(
                        unparsedText));
                context.write(new Text(state), hispanicRecord);
            }
        }
    }

    public Long getMaleHispanicBelow18(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(3864, 3973, unparsedText);
    }

    public Long getFemaleHispanicBelow18(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(4143, 4252, unparsedText);
    }

    public Long getMaleHispanic19to29(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(3981, 4018, unparsedText);
    }

    public Long getFemaleHispanic19to29(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(4260, 4297, unparsedText);
    }

    public Long getMaleHispanic30to39(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(4026, 4036, unparsedText);
    }

    public Long getFemaleHispanic30to39(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(4305, 4315, unparsedText);
    }

    public long getTotalMalePopulation(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(3864, 4135, unparsedText);
    }

    public long getTotalFemalePopulation(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(4143, 4414, unparsedText);
    }
}
