package mappers;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import records.MarriageRecord;
import util.RecordParsingUtils;

public class MarriageMapper extends Mapper<LongWritable, Text, Text, MarriageRecord> {

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

            MarriageRecord marriageRecord = new MarriageRecord();

            if (!logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                marriageRecord.setMaleNeverMarried(getMaleNeverMarried(unparsedText));
                marriageRecord.setFemaleNeverMarried(getFemaleNeverMarried(unparsedText));
                marriageRecord.setPopulation(getPopulation(unparsedText));
                context.write(new Text(state), marriageRecord);
            }
        }
    }

    public Long getPopulation(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(300, 309, unparsedText);
    }

    public Long getMaleNeverMarried(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(4422, 4431, unparsedText);
    }

    public Long getFemaleNeverMarried(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(4467, 4476, unparsedText);
    }
}
