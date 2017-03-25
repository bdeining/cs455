package housing;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import record.HousingMedianRecord;
import util.RecordParsingUtils;

public class HousingMedianMapper extends Mapper<LongWritable, Text, Text, HousingMedianRecord> {

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

            HousingMedianRecord housingMedianRecord = new HousingMedianRecord();

            if (logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                housingMedianRecord.setMap(getHousingValues(unparsedText));
                housingMedianRecord.setLogicalRecordPartNumber(logicalRecordPartNumber);
                housingMedianRecord.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
                context.write(new Text(state), housingMedianRecord);
            }
        }
    }

    private Map<String, Long> getHousingValues(String unparsedText) {
        Map<String, Long> stringLongMap = new LinkedHashMap<>();

        int count = 0;
        for (int i = 2928; i < 3100; i += 9) {
            long number = Long.parseLong(unparsedText.substring(i, i + 9));
            stringLongMap.put(HousingMedianRecord.VALUE_LIST.get(count), number);
            count++;
        }

        return stringLongMap;
    }
}
