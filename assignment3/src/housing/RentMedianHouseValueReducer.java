package housing;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.RentMedianHouseValueRecord;

public class RentMedianHouseValueReducer
        extends Reducer<Text, RentMedianHouseValueRecord, Text, RentMedianHouseValueRecord> {
    @Override
    protected void reduce(Text key, Iterable<RentMedianHouseValueRecord> values, Context context)
            throws IOException, InterruptedException {
        RentMedianHouseValueRecord marriageRecord = new RentMedianHouseValueRecord();

        long rentTenure = 0;
        long unmarriedPopulation = 0;

        for (RentMedianHouseValueRecord val : values) {
            unmarriedPopulation += val.getUnmarriedPopulation();
            rentTenure += val.getRentTenure();
        }

        marriageRecord.setRentTenure(rentTenure);
        marriageRecord.setUnmarriedPopulation(unmarriedPopulation);
        context.write(key, marriageRecord);
    }
}