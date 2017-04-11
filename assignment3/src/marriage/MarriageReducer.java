package marriage;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.MarriageRecord;

public class MarriageReducer extends Reducer<Text, MarriageRecord, Text, MarriageRecord> {
    @Override
    protected void reduce(Text key, Iterable<MarriageRecord> values, Context context)
            throws IOException, InterruptedException {
        long maleNeverMarried = 0;
        long femaleNeverMarried = 0;
        long population = 0;

        for (MarriageRecord val : values) {
            maleNeverMarried += val.getMaleNeverMarried();
            femaleNeverMarried += val.getFemaleNeverMarried();
            population += val.getPopulation();
        }

        MarriageRecord marriageRecord = new MarriageRecord();
        marriageRecord.setMaleNeverMarried(maleNeverMarried);
        marriageRecord.setFemaleNeverMarried(femaleNeverMarried);
        marriageRecord.setPopulation(population);
        context.write(key, marriageRecord);
    }
}