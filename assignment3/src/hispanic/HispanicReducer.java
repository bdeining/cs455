package hispanic;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import record.HispanicRecord;

public class HispanicReducer extends Reducer<Text, HispanicRecord, Text, HispanicRecord> {
    @Override
    protected void reduce(Text key, Iterable<HispanicRecord> values, Context context)
            throws IOException, InterruptedException {
        long maleHispanic0to18 = 0;
        long maleHispanic19to29 = 0;
        long maleHispanic30to39 = 0;

        long femaleHispanic0to18 = 0;
        long femaleHispanic19to29 = 0;
        long femaleHispanic30to39 = 0;

        long totalFemalePopulation = 0;
        long totalMalePopulation = 0;

        for (HispanicRecord val : values) {
            maleHispanic0to18 += val.getMaleHispanic0to18();
            maleHispanic19to29 += val.getMaleHispanic19to29();
            maleHispanic30to39 += val.getMaleHispanic30to39();

            femaleHispanic0to18 += val.getFemaleHispanic0to18();
            femaleHispanic19to29 += val.getFemaleHispanic19to29();
            femaleHispanic30to39 += val.getFemaleHispanic30to39();

            totalFemalePopulation += val.getTotalFemaleHispanicPopulation();
            totalMalePopulation += val.getTotalMaleHispanicPopulation();
        }

        HispanicRecord marriageRecord = new HispanicRecord();
        marriageRecord.setMaleHispanic0to18(maleHispanic0to18);
        marriageRecord.setFemaleHispanic0to18(femaleHispanic0to18);
        marriageRecord.setMaleHispanic19to29(maleHispanic19to29);
        marriageRecord.setFemaleHispanic19to29(femaleHispanic19to29);
        marriageRecord.setMaleHispanic30to39(maleHispanic30to39);
        marriageRecord.setFemaleHispanic30to39(femaleHispanic30to39);
        marriageRecord.setTotalFemaleHispanicPopulation(totalFemalePopulation);
        marriageRecord.setTotalMaleHispanicPopulation(totalMalePopulation);
        context.write(key, marriageRecord);
    }
}