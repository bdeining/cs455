package reducers;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import records.ElderlyPopulationRecord;

public class ElderlyPopulationReducer
        extends Reducer<Text, ElderlyPopulationRecord, Text, DoubleWritable> {

    private String state = "";

    private double percent = 0.0;

    @Override
    protected void reduce(Text key, Iterable<ElderlyPopulationRecord> values, Context context)
            throws IOException, InterruptedException {

        for (ElderlyPopulationRecord val : values) {
            double statePercent = (val.getElderlyPopulation() / val.getTotalPopulation()) * 100;
            if (statePercent > percent) {
                percent = statePercent;
                state = key.toString();
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        context.write(new Text(state), new DoubleWritable(percent));
    }
}