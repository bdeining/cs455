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
    protected void reduce(Text key, Iterable<Tenure> values, Context context)
            throws IOException, InterruptedException {
        long owned = 0;
        long rented = 0;
        long population = 0;
        long maleNeverMarried = 0;
        long femaleNeverMarried = 0;
        long logicalPart = 0;
        long logicalTotalPart = 0;

        long hispanicBelow18 = 0;
        long hispanic19to29 = 0;
        long hispanic30to39 = 0;

        String type = "all";

        long urban = 0;
        long rural = 0;

        // calculate the total count
        for (Tenure val : values) {
            owned += val.getOwned();
            rented += val.getRented();
            population += val.getPopulation();
            logicalPart = val.getLogicalRecordPartNumber();
            logicalTotalPart = val.getTotalNumberOfPartsInRecord();
            maleNeverMarried += val.getMaleNeverMarried();
            femaleNeverMarried += val.getFemaleNeverMarried();

            hispanicBelow18 += val.getHispanic0to18();
            hispanic19to29 += val.getHispanic19to29();
            hispanic30to39 += val.getHispanic30to39();

            type = val.getType();

            urban += val.getUrban();
            rural += val.getRural();
        }

        Tenure tenure = new Tenure();
        tenure.setType(type);
        tenure.setOwned(owned);
        tenure.setRented(rented);
        tenure.setPopulation(population);
        tenure.setMaleNeverMarried(maleNeverMarried);
        tenure.setFemaleNeverMarried(femaleNeverMarried);
        tenure.setLogicalRecordPartNumber(logicalPart);
        tenure.setTotalNumberOfPartsInRecord(logicalTotalPart);

        tenure.setHispanic0to18(hispanicBelow18);
        tenure.setHispanic19to29(hispanic19to29);
        tenure.setHispanic30to39(hispanic30to39);

        tenure.setRural(rural);
        tenure.setUrban(urban);
        context.write(key, tenure);
    }
}
