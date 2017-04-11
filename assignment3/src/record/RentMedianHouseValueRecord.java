package record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class RentMedianHouseValueRecord implements Writable {

    private long unmarriedPopulation;

    private long rentTenure;

    public RentMedianHouseValueRecord() {

    }

    public long getUnmarriedPopulation() {
        return unmarriedPopulation;
    }

    public void setUnmarriedPopulation(long unmarriedPopulation) {
        this.unmarriedPopulation = unmarriedPopulation;
    }

    public long getRentTenure() {
        return rentTenure;
    }

    public void setRentTenure(long rentTenure) {
        this.rentTenure = rentTenure;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        ;
        dataOutput.writeLong(rentTenure);
        dataOutput.writeLong(unmarriedPopulation);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
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
