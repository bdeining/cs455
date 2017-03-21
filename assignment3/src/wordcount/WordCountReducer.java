package wordcount;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reducer: Input to the reducer is the output from the mapper. It receives word, list<count> pairs.
 * Sums up individual counts per given word. Emits <word, total count> pairs.
 */
public class WordCountReducer extends Reducer<Text, Tenure, Text, Tenure> {
    @Override
    protected void reduce(Text key, Iterable<Tenure> values, Context context) throws IOException, InterruptedException {
        long owned = 0;
        long rented = 0;
        long population = 0;
        long logicalPart = 0;
        long logicalTotalPart = 0;


        // calculate the total count
        for(Tenure val : values){
            owned += val.getOwned();
            rented += val.getRented();
            population += val.getPopulation();
            logicalPart = val.getLogicalRecordPartNumber();
            logicalTotalPart = val.getTotalNumberOfPartsInRecord();
        }

        Tenure tenure = new Tenure(rented, owned, population);
        tenure.setLogicalRecordPartNumber(logicalPart);
        tenure.setTotalNumberOfPartsInRecord(logicalTotalPart);
        context.write(key, tenure);
    }
}
