package population;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.ElderlyPopulationRecord;

public class ElderlyPopulationCombiner
        extends Reducer<Text, ElderlyPopulationRecord, Text, ElderlyPopulationRecord> {
    @Override
    protected void reduce(Text key, Iterable<ElderlyPopulationRecord> values, Context context)
            throws IOException, InterruptedException {
        double totalPopulation = 0.0;
        double elderlyPopulation = 0.0;

        for (ElderlyPopulationRecord val : values) {
            totalPopulation += val.getTotalPopulation();
            elderlyPopulation += val.getElderlyPopulation();
        }

        ElderlyPopulationRecord elderlyPopulationRecord = new ElderlyPopulationRecord();
        elderlyPopulationRecord.setPopulation(elderlyPopulation, totalPopulation);
        context.write(key, elderlyPopulationRecord);
    }
}