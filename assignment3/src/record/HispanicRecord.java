package record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class HispanicRecord implements Writable {
    private long maleHispanic0to18 = 0;

    private long maleHispanic19to29 = 0;

    private long maleHispanic30to39 = 0;

    private long femaleHispanic0to18 = 0;

    private long femaleHispanic19to29 = 0;

    private long femaleHispanic30to39 = 0;

    private long totalFemaleHispanicPopulation = 0;

    private long totalMaleHispanicPopulation = 0;

    public HispanicRecord() {

    }

    public long getTotalFemaleHispanicPopulation() {
        return totalFemaleHispanicPopulation;
    }

    public long getTotalMaleHispanicPopulation() {
        return totalMaleHispanicPopulation;
    }

    public void setTotalFemaleHispanicPopulation(long totalFemaleHispanicPopulation) {
        this.totalFemaleHispanicPopulation = totalFemaleHispanicPopulation;
    }

    public void setTotalMaleHispanicPopulation(long totalMaleHispanicPopulation) {
        this.totalMaleHispanicPopulation = totalMaleHispanicPopulation;
    }

    public long getMaleHispanic0to18() {
        return maleHispanic0to18;
    }

    public void setMaleHispanic0to18(long maleHispanic0to18) {
        this.maleHispanic0to18 = maleHispanic0to18;
    }

    public long getMaleHispanic19to29() {
        return maleHispanic19to29;
    }

    public void setMaleHispanic19to29(long maleHispanic19to29) {
        this.maleHispanic19to29 = maleHispanic19to29;
    }

    public long getMaleHispanic30to39() {
        return maleHispanic30to39;
    }

    public void setFemaleHispanic30to39(long femaleHispanic30to39) {
        this.femaleHispanic30to39 = femaleHispanic30to39;
    }

    public long getFemaleHispanic0to18() {
        return femaleHispanic0to18;
    }

    public void setFemaleHispanic0to18(long femaleHispanic0to18) {
        this.femaleHispanic0to18 = femaleHispanic0to18;
    }

    public long getFemaleHispanic19to29() {
        return femaleHispanic19to29;
    }

    public void setFemaleHispanic19to29(long femaleHispanic19to29) {
        this.femaleHispanic19to29 = femaleHispanic19to29;
    }

    public long getFemaleHispanic30to39() {
        return femaleHispanic30to39;
    }

    public void setMaleHispanic30to39(long maleHispanic30to39) {
        this.maleHispanic30to39 = maleHispanic30to39;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(maleHispanic0to18);
        dataOutput.writeLong(maleHispanic19to29);
        dataOutput.writeLong(maleHispanic30to39);
        dataOutput.writeLong(femaleHispanic0to18);
        dataOutput.writeLong(femaleHispanic19to29);
        dataOutput.writeLong(femaleHispanic30to39);
        dataOutput.writeLong(totalFemaleHispanicPopulation);
        dataOutput.writeLong(totalMaleHispanicPopulation);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        maleHispanic0to18 = dataInput.readLong();
        maleHispanic19to29 = dataInput.readLong();
        maleHispanic30to39 = dataInput.readLong();
        femaleHispanic0to18 = dataInput.readLong();
        femaleHispanic19to29 = dataInput.readLong();
        femaleHispanic30to39 = dataInput.readLong();
        totalFemaleHispanicPopulation = dataInput.readLong();
        totalMaleHispanicPopulation = dataInput.readLong();
    }

    @Override
    public String toString() {
        return getHispanicString();
    }

    private String getHispanicString() {
        if (getFemaleHispanic0to18() == 0 ||
                getFemaleHispanic19to29() == 0 ||
                getFemaleHispanic30to39() == 0 ||
                getMaleHispanic30to39() == 0 ||
                getMaleHispanic19to29() == 0 ||
                getMaleHispanic0to18() == 0 ||
                totalMaleHispanicPopulation == 0 ||
                totalFemaleHispanicPopulation == 0) {
            return "";
        }

        double maleBelow18Percentage = getMaleHispanic0to18() / (double) totalMaleHispanicPopulation * 100;
        double male19to29Percentage = getMaleHispanic19to29() / (double)totalMaleHispanicPopulation * 100;
        double male30to39Percentage = getMaleHispanic30to39() / (double)totalMaleHispanicPopulation * 100;

        double femaleBelow18Percentage =
                getFemaleHispanic0to18() / (double)totalFemaleHispanicPopulation * 100;
        double female19to29Percentage =
                getFemaleHispanic19to29() /(double) totalFemaleHispanicPopulation * 100;
        double female30to39Percentage =
                getFemaleHispanic30to39() /(double) totalFemaleHispanicPopulation * 100;

        return String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                (totalMaleHispanicPopulation + totalFemaleHispanicPopulation),
                totalMaleHispanicPopulation,
                totalFemaleHispanicPopulation,
                getMaleHispanic0to18(),
                maleBelow18Percentage,
                getMaleHispanic19to29(),
                male19to29Percentage,
                getMaleHispanic30to39(),
                male30to39Percentage,
                getFemaleHispanic0to18(),
                femaleBelow18Percentage,
                getFemaleHispanic19to29(),
                female19to29Percentage,
                getFemaleHispanic30to39(),
                female30to39Percentage
        );
    }
}
