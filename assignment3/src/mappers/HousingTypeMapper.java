package mappers;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import records.HousingTypeRecord;
import util.RecordParsingUtils;

public class HousingTypeMapper extends Mapper<LongWritable, Text, Text, HousingTypeRecord> {

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

            HousingTypeRecord housingTypeRecord = new HousingTypeRecord();

            if (logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                housingTypeRecord.setRural(getRuralOccupied(unparsedText));
                housingTypeRecord.setUrban(getUrbanOccupied(unparsedText));
                context.write(new Text(state), housingTypeRecord);
            }
        }
    }

    public Long getUrbanOccupied(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(1821, 1831, unparsedText);
    }

    public Long getRuralOccupied(String unparsedText) {
        return RecordParsingUtils.parseTextIntoLong(1839, 1848, unparsedText);
    }
}
