package tenure;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.TenureRecord;

public class TenureReducer  extends Reducer<Text, TenureRecord, Text, TenureRecord> {
    @Override
    protected void reduce(Text key, Iterable<TenureRecord> values, Context context) throws IOException, InterruptedException {
        long rented = 0;
        long owned = 0;
        long totalNumberOfPartsInRecord = 0;
        long logicalRecodPartNumber = 0;

        for(TenureRecord val : values){
            rented += val.getRented();
            owned += val.getOwned();
            totalNumberOfPartsInRecord = val.getTotalNumberOfPartsInRecord();
            logicalRecodPartNumber = val.getLogicalRecordPartNumber();
        }

        TenureRecord tenureRecord = new TenureRecord();
        tenureRecord.setOwned(owned);
        tenureRecord.setRented(rented);
        tenureRecord.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
        tenureRecord.setLogicalRecordPartNumber(logicalRecodPartNumber);
        context.write(key, tenureRecord);
    }


}