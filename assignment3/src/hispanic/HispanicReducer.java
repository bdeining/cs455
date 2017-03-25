package hispanic;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.HispanicRecord;

public class HispanicReducer extends Reducer<Text, HispanicRecord, Text, HispanicRecord> {
    @Override
    protected void reduce(Text key, Iterable<HispanicRecord> values, Context context)
            throws IOException, InterruptedException {
        long hispanic0to18 = 0;
        long hispanic19to29 = 0;
        long hispanic30to39 = 0;

        long totalNumberOfPartsInRecord = 0;
        long logicalRecodPartNumber = 0;

        for (HispanicRecord val : values) {
            hispanic0to18 += val.getHispanic0to18();
            hispanic19to29 += val.getHispanic19to29();
            hispanic30to39 += val.getHispanic30to39();
            totalNumberOfPartsInRecord = val.getTotalNumberOfPartsInRecord();
            logicalRecodPartNumber = val.getLogicalRecordPartNumber();
        }

        HispanicRecord marriageRecord = new HispanicRecord();
        marriageRecord.setHispanic0to18(hispanic0to18);
        marriageRecord.setHispanic19to29(hispanic19to29);
        marriageRecord.setHispanic30to39(hispanic30to39);
        marriageRecord.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
        marriageRecord.setLogicalRecordPartNumber(logicalRecodPartNumber);
        context.write(key, marriageRecord);
    }
}