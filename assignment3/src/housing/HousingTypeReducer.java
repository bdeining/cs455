package housing;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.HousingTypeRecord;

public class HousingTypeReducer extends Reducer<Text, HousingTypeRecord, Text, HousingTypeRecord> {
    @Override
    protected void reduce(Text key, Iterable<HousingTypeRecord> values, Context context)
            throws IOException, InterruptedException {
        long rural = 0;
        long urban = 0;

        for (HousingTypeRecord val : values) {
            rural += val.getRural();
            urban += val.getUrban();
        }

        HousingTypeRecord housingTypeRecord = new HousingTypeRecord();
        housingTypeRecord.setRural(rural);
        housingTypeRecord.setUrban(urban);
        context.write(key, housingTypeRecord);
    }

}