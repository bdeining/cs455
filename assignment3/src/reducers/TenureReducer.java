package reducers;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import records.TenureRecord;

public class TenureReducer extends Reducer<Text, TenureRecord, Text, TenureRecord> {
    @Override
    protected void reduce(Text key, Iterable<TenureRecord> values, Context context)
            throws IOException, InterruptedException {
        long rented = 0;
        long owned = 0;

        for (TenureRecord val : values) {
            rented += val.getRented();
            owned += val.getOwned();
        }

        TenureRecord tenureRecord = new TenureRecord();
        tenureRecord.setOwned(owned);
        tenureRecord.setRented(rented);
        context.write(key, tenureRecord);
    }

}