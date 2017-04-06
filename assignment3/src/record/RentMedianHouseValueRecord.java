package record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class RentMedianHouseValueRecord extends Record {

    private long unmarriedPopulation;

    private long rentTenure;

    public RentMedianHouseValueRecord() {

    }

    public void setUnmarriedPopulation(long unmarriedPopulation) {
        this.unmarriedPopulation = unmarriedPopulation;
    }

    public long getUnmarriedPopulation() {
        return unmarriedPopulation;
    }

    public void setRentTenure(long rentTenure) {
        this.rentTenure = rentTenure;
    }

    public long getRentTenure() {
        return rentTenure;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        super.write(dataOutput);
        dataOutput.writeLong(rentTenure);
        dataOutput.writeLong(unmarriedPopulation);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        super.readFields(dataInput);
        rentTenure = dataInput.readLong();
        unmarriedPopulation = dataInput.readLong();
    }

    @Override
    public String toString() {
        return generateHispanicString();
    }

    private String generateHispanicString() {
        return String.format("%s\t%s", rentTenure, unmarriedPopulation);
    }
}
