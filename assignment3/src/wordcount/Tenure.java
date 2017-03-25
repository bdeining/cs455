package wordcount;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Tenure implements Writable {

    private long rented = 0;

    private long owned = 0;

    private long logicalRecordPartNumber = 0;

    private long population = 0;

    private long totalNumberOfPartsInRecord = 0;

    private long maleNeverMarried = 0;

    private long femaleNeverMarried = 0;

    private long hispanic0to18 = 0;

    private long hispanic19to29 = 0;

    private long hispanic30to39 = 0;

    private long rural = 0;

    private long urban = 0;

    private String medianHousingValue = "";

    private String type = "all";

    public Tenure() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getLogicalRecordPartNumber() {
        return logicalRecordPartNumber;
    }

    public void setLogicalRecordPartNumber(long logicalRecordPartNumber) {
        this.logicalRecordPartNumber = logicalRecordPartNumber;
    }

    public long getTotalNumberOfPartsInRecord() {
        return totalNumberOfPartsInRecord;
    }

    public void setTotalNumberOfPartsInRecord(long totalNumberOfPartsInRecord) {
        this.totalNumberOfPartsInRecord = totalNumberOfPartsInRecord;
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

    public String getMedianHousingValue() {
        return medianHousingValue;
    }

    public void setMedianHousingValue(String medianHousingValue) {
        this.medianHousingValue = medianHousingValue;
    }

    public long getRented() {
        return rented;
    }

    public void setRented(long rented) {
        this.rented = rented;
    }

    public long getOwned() {
        return owned;
    }

    public void setOwned(long owned) {
        this.owned = owned;
    }

    public long getHispanic0to18() {
        return hispanic0to18;
    }

    public void setHispanic0to18(long hispanic0to18) {
        this.hispanic0to18 = hispanic0to18;
    }

    public long getHispanic19to29() {
        return hispanic19to29;
    }

    public void setHispanic19to29(long hispanic19to29) {
        this.hispanic19to29 = hispanic19to29;
    }

    public long getHispanic30to39() {
        return hispanic30to39;
    }

    public void setHispanic30to39(long hispanic30to39) {
        this.hispanic30to39 = hispanic30to39;
    }

    public long getRural() {
        return rural;
    }

    public void setRural(long rural) {
        this.rural = rural;
    }

    public long getUrban() {
        return urban;
    }

    public void setUrban(long urban) {
        this.urban = urban;
    }

    @Override
    public String toString() {
        switch (type) {
        case "tenure":
            return getTenureString();
        case "marriage":
            return getMarriageString();
        case "hispanic":
            return getHispanicString();
        case "homes":
            return getHousingString();
        default:
            return "";
        }
    }

    public String getTenureString() {
        double totalHousing = getOwned() + getRented();
        double ownerPercentage = getOwned() / totalHousing * 100;
        double renterPercentage = getRented() / totalHousing * 100;
        return String.format("%s,%s,%s,%s",
                getOwned(),
                getRented(),
                ownerPercentage,
                renterPercentage);
    }

    private String getMarriageString() {
        if (getMaleNeverMarried() == 0 || getFemaleNeverMarried() == 0) {
            return "";
        }

        double population = getPopulation();
        double maleNeverMarriedPercentage = getMaleNeverMarried() / population * 100;
        double femaleNeverMarriedPercentage = getFemaleNeverMarried() / population * 100;

        return String.format("%s,%s,%s,%s",
                getMaleNeverMarried(),
                getFemaleNeverMarried(),
                maleNeverMarriedPercentage,
                femaleNeverMarriedPercentage);
    }

    private String getHispanicString() {
        if (getHispanic30to39() == 0 || getHispanic19to29() == 0 || getHispanic0to18() == 0) {
            return "";
        }

        double totalHispanic = getHispanic0to18() + getHispanic19to29() + getHispanic30to39();
        double below18Percentage = getHispanic0to18() / totalHispanic * 100;
        double b19to29Percentage = getHispanic19to29() / totalHispanic * 100;
        double b30to39Percentage = getHispanic30to39() / totalHispanic * 100;
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                totalHispanic,
                getHispanic0to18(),
                below18Percentage,
                getHispanic19to29(),
                b19to29Percentage,
                getHispanic30to39(),
                b30to39Percentage);

    }

    private String getHousingString() {
        if (getUrban() == 0 || getRural() == 0) {
            return "";
        }

        return String.format("%s,%s", rural, urban);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(owned);
        dataOutput.writeLong(rented);
        dataOutput.writeLong(population);
        dataOutput.writeLong(logicalRecordPartNumber);
        dataOutput.writeLong(totalNumberOfPartsInRecord);
        dataOutput.writeLong(maleNeverMarried);
        dataOutput.writeLong(femaleNeverMarried);

        dataOutput.writeLong(hispanic0to18);
        dataOutput.writeLong(hispanic19to29);
        dataOutput.writeLong(hispanic30to39);

        dataOutput.writeLong(rural);
        dataOutput.writeLong(urban);

        byte[] bytes = type.getBytes();
        dataOutput.writeInt(bytes.length);
        dataOutput.write(bytes);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        owned = dataInput.readLong();
        rented = dataInput.readLong();
        population = dataInput.readLong();
        logicalRecordPartNumber = dataInput.readLong();
        totalNumberOfPartsInRecord = dataInput.readLong();
        maleNeverMarried = dataInput.readLong();
        femaleNeverMarried = dataInput.readLong();

        hispanic0to18 = dataInput.readLong();
        hispanic19to29 = dataInput.readLong();
        hispanic30to39 = dataInput.readLong();

        rural = dataInput.readLong();
        urban = dataInput.readLong();

        int byteLength = dataInput.readInt();
        byte[] bytes = new byte[byteLength];
        dataInput.readFully(bytes);
        type = new String(bytes);

    }
}
