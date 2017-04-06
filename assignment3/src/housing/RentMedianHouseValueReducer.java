package housing;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.HousingMedianRecord;
import record.RentMedianHouseValueRecord;

public class RentMedianHouseValueReducer extends Reducer<Text, RentMedianHouseValueRecord, Text, RentMedianHouseValueRecord> {
    @Override
    protected void reduce(Text key, Iterable<RentMedianHouseValueRecord> values, Context context)
            throws IOException, InterruptedException {

        RentMedianHouseValueRecord marriageRecord = new RentMedianHouseValueRecord();

        long rentTenure = 0;
        long unmarriedPopulation = 0;

        long totalNumberOfPartsInRecord = 0;
        long logicalRecodPartNumber = 0;

        for (RentMedianHouseValueRecord val : values) {
            unmarriedPopulation +=  val.getUnmarriedPopulation();
            rentTenure += val.getRentTenure();
            totalNumberOfPartsInRecord = val.getTotalNumberOfPartsInRecord();
            logicalRecodPartNumber = val.getLogicalRecordPartNumber();
        }


        marriageRecord.setRentTenure(rentTenure);
        marriageRecord.setUnmarriedPopulation(unmarriedPopulation);
        marriageRecord.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
        marriageRecord.setLogicalRecordPartNumber(logicalRecodPartNumber);
        context.write(key, marriageRecord);
    }
}