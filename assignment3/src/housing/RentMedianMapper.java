package housing;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import record.RentMedianRecord;
import util.RecordParsingUtils;

public class RentMedianMapper extends Mapper<LongWritable, Text, Text, RentMedianRecord> {

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

            RentMedianRecord rentMedianRecord = new RentMedianRecord();

            if (logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                rentMedianRecord.setMap(getHousingValues(unparsedText));
                rentMedianRecord.setLogicalRecordPartNumber(logicalRecordPartNumber);
                rentMedianRecord.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
                context.write(new Text(state), rentMedianRecord);
            }
        }
    }

    private Map<String, Long> getHousingValues(String unparsedText) {
        Map<String, Long> stringLongMap = new LinkedHashMap<>();

        int count = 0;
        for (int i = 3450; i < 3595; i += 9) {
            long number = Long.parseLong(unparsedText.substring(i, i + 9));
            stringLongMap.put(RentMedianRecord.VALUE_LIST.get(count), number);
            count++;
        }

        return stringLongMap;
    }
}