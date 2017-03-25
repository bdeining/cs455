package record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class MarriageRecord extends Record {
    private long maleNeverMarried = 0;

    private long femaleNeverMarried = 0;

    private long population = 0;

    public MarriageRecord() {

    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getMaleNeverMarried() {
        return maleNeverMarried;
    }

    public void setMaleNeverMarried(long maleNeverMarried) {
        this.maleNeverMarried = maleNeverMarried;
    }

    public long getFemaleNeverMarried() {
        return femaleNeverMarried;
    }

    public void setFemaleNeverMarried(long femaleNeverMarried) {
        this.femaleNeverMarried = femaleNeverMarried;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        super.write(dataOutput);
        dataOutput.writeLong(maleNeverMarried);
        dataOutput.writeLong(femaleNeverMarried);
        dataOutput.writeLong(population);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        super.readFields(dataInput);
        maleNeverMarried = dataInput.readLong();
        femaleNeverMarried = dataInput.readLong();
        population = dataInput.readLong();
    }

    @Override
    public String toString() {
        return getMarriageString();
    }

    private String getMarriageString() {
        if (getMaleNeverMarried() == 0 || getFemaleNeverMarried() == 0) {
            return "";
        }

        double population = getPopulation();
        double maleNeverMarriedPercentage = getMaleNeverMarried() / population * 100;
        double femaleNeverMarriedPercentage = getFemaleNeverMarried() / population * 100;

        return String.format("%s\t%s\t%s\t%s",
                getMaleNeverMarried(),
                getFemaleNeverMarried(),
                maleNeverMarriedPercentage,
                femaleNeverMarriedPercentage);
    }
}
