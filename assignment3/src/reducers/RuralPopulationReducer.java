package reducers;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import records.RuralPopulationRecord;

public class RuralPopulationReducer
        extends Reducer<Text, RuralPopulationRecord, Text, RuralPopulationRecord> {
    @Override
    protected void reduce(Text key, Iterable<RuralPopulationRecord> values, Context context)
            throws IOException, InterruptedException {
        RuralPopulationRecord marriageRecord = new RuralPopulationRecord();

        long totalPopulation = 0;
        long urbanPopulation = 0;
        long ruralPopulation = 0;

        for (RuralPopulationRecord val : values) {
            totalPopulation += val.getPopulation();
            urbanPopulation += val.getUrbanPopulation();
            ruralPopulation += val.getRuralPopulation();
        }

        marriageRecord.setRuralPopulation(ruralPopulation);
        marriageRecord.setUrbanPopulation(urbanPopulation);
        marriageRecord.setPopulation(totalPopulation);
        context.write(key, marriageRecord);
    }
}