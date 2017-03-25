package marriage;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import record.MarriageRecord;
import util.RecordParsingUtils;

public class MarriageMapper extends Mapper<LongWritable, Text, Text, MarriageRecord> {

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

            MarriageRecord marriageRecord = new MarriageRecord();

            if (!logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                marriageRecord.setMaleNeverMarried(getMaleNeverMarried(unparsedText));
                marriageRecord.setFemaleNeverMarried(getFemaleNeverMarried(unparsedText));
                marriageRecord.setPopulation(getPopulation(unparsedText));
                marriageRecord.setLogicalRecordPartNumber(logicalRecordPartNumber);
                marriageRecord.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
                context.write(new Text(state), marriageRecord);
            }
        }
    }

    public Long getPopulation(String unparsedText) {
        Long population = Long.parseLong(unparsedText.substring(300, 309));
        return population;
    }

    public Long getMaleNeverMarried(String unparsedText) {
        Long maleNeverMarried = Long.parseLong(unparsedText.substring(4422, 4431));
        return maleNeverMarried;
    }

    public Long getFemaleNeverMarried(String unparsedText) {
        Long femaleNeverMarried = Long.parseLong(unparsedText.substring(4467, 4476));
        return femaleNeverMarried;
    }
}
