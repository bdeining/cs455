package housing;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.HousingMedianRecord;

public class HousingMedianReducer
        extends Reducer<Text, HousingMedianRecord, Text, HousingMedianRecord> {
    @Override
    protected void reduce(Text key, Iterable<HousingMedianRecord> values, Context context)
            throws IOException, InterruptedException {
        HousingMedianRecord housingMedianRecord = new HousingMedianRecord();

        for (HousingMedianRecord val : values) {
            combineMap(housingMedianRecord, val.getMap());
        }
        context.write(key, housingMedianRecord);
    }

    private void combineMap(HousingMedianRecord housingMedianRecord, Map<String, Long> map) {
        Map<String, Long> fullMap = housingMedianRecord.getMap();
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            long value = fullMap.get(entry.getKey());
            value += entry.getValue();
            fullMap.put(entry.getKey(), value);
        }

        housingMedianRecord.setMap(fullMap);
    }
}