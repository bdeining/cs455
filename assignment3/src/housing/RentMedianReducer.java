package housing;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.RentMedianRecord;

public class RentMedianReducer extends Reducer<Text, RentMedianRecord, Text, RentMedianRecord> {
    @Override
    protected void reduce(Text key, Iterable<RentMedianRecord> values, Context context)
            throws IOException, InterruptedException {
        RentMedianRecord rentMedianRecord = new RentMedianRecord();
        long totalNumberOfPartsInRecord = 0;
        long logicalRecodPartNumber = 0;

        for (RentMedianRecord val : values) {
            combineMap(rentMedianRecord, val.getMap());
            totalNumberOfPartsInRecord = val.getTotalNumberOfPartsInRecord();
            logicalRecodPartNumber = val.getLogicalRecordPartNumber();
        }

        rentMedianRecord.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
        rentMedianRecord.setLogicalRecordPartNumber(logicalRecodPartNumber);
        context.write(key, rentMedianRecord);
    }

    private void combineMap(RentMedianRecord rentMedianRecord, Map<String, Long> map) {
        Map<String, Long> fullMap = rentMedianRecord.getMap();
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            long value = fullMap.get(entry.getKey());
            value += entry.getValue();
            fullMap.put(entry.getKey(), value);
        }

        rentMedianRecord.setMap(fullMap);
    }
}