package mappers;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import records.RuralPopulationRecord;
import util.RecordParsingUtils;

public class RuralPopulationMapper extends Mapper<LongWritable, Text, Text, RuralPopulationRecord> {

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

            RuralPopulationRecord ruralPopulationRecord = new RuralPopulationRecord();

            if (!logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                ruralPopulationRecord.setPopulation(getPopulation(unparsedText));
                ruralPopulationRecord.setOutsideUrbanPopulation(getOutsideUrbanPopulation(
                        unparsedText));
                ruralPopulationRecord.setUrbanPopulation(getUrbanPopulation(unparsedText));
                context.write(new Text(state), ruralPopulationRecord);
            }
        }
    }

    private Long getPopulation(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(300, 309, unparsedText);
    }

    private Long getOutsideUrbanPopulation(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(327, 336, unparsedText);
    }

    private Long getUrbanPopulation(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(336, 345, unparsedText);
    }
}
