package housing;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import record.HousingTypeRecord;
import util.RecordParsingUtils;

public class HousingTypeMapper extends Mapper<LongWritable, Text, Text, HousingTypeRecord> {

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

            HousingTypeRecord housingTypeRecord = new HousingTypeRecord();

            if (logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                housingTypeRecord.setRural(getRuralOccupied(unparsedText));
                housingTypeRecord.setUrban(getUrbanOccupied(unparsedText));

                housingTypeRecord.setLogicalRecordPartNumber(logicalRecordPartNumber);
                housingTypeRecord.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
                context.write(new Text(state), housingTypeRecord);
            }
        }
    }

    public Long getUrbanOccupied(String unparsedText) {
        Long urban = 0L;
        urban += Long.parseLong(unparsedText.substring(1821, 1830));
        urban += Long.parseLong(unparsedText.substring(1830, 1839));
        return urban;
    }

    public Long getRuralOccupied(String unparsedText) {
        Long rural = Long.parseLong(unparsedText.substring(1839, 1848));
        return rural;
    }
}
