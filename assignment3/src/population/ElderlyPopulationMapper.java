package population;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import record.ElderlyPopulationRecord;
import util.RecordParsingUtils;

public class ElderlyPopulationMapper
        extends Mapper<LongWritable, Text, Text, ElderlyPopulationRecord> {

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

            ElderlyPopulationRecord elderlyPopulationRecord = new ElderlyPopulationRecord();

            if (!logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                elderlyPopulationRecord.setPopulation(getElderlyPopulation(unparsedText),
                        getPopulation(unparsedText));
                context.write(new Text(state), elderlyPopulationRecord);
            }
        }
    }

    public Long getPopulation(String unparsedText) {
        Long population = Long.parseLong(unparsedText.substring(300, 309));
        return population;
    }

    public Long getElderlyPopulation(String unparsedText) {
        Long elderlyPopulation = Long.parseLong(unparsedText.substring(1065, 1074));
        return elderlyPopulation;
    }

}
