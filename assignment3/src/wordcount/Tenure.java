package wordcount;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Tenure implements Writable {

    private long total;

    private long rented;

    private long owned;

    private long logicalRecordPartNumber;

    private long population;

    private long totalNumberOfPartsInRecord;

    public Tenure() {

    }

    public Tenure(Long rented, Long owned, Long population) {
        this.owned = owned;
        this.rented = rented;
        this.population = population;
        this.total = owned + rented;
    }

    public double getTotal() {
        return total;
    }

    public void setLogicalRecordPartNumber(long logicalRecordPartNumber) {
        this.logicalRecordPartNumber = logicalRecordPartNumber;
    }

    public void setTotalNumberOfPartsInRecord(long totalNumberOfPartsInRecord) {
        this.totalNumberOfPartsInRecord = totalNumberOfPartsInRecord;
    }

    public long getLogicalRecordPartNumber() {
        return logicalRecordPartNumber;
    }

    public long getTotalNumberOfPartsInRecord() {
        return totalNumberOfPartsInRecord;
    }

    public double getRented() {
        return rented;
    }

    public double getOwned() {
        return owned;
    }

    public double getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        String toString = String.format("Owned : %15s Rented : %15s Population : %15s Record Number : %15s Total Number : %15s", getOwned(), getRented(), getPopulation(), logicalRecordPartNumber, totalNumberOfPartsInRecord);
        return toString;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(owned);
        dataOutput.writeLong(rented);
        dataOutput.writeLong(population);
        dataOutput.writeLong(logicalRecordPartNumber);
        dataOutput.writeLong(totalNumberOfPartsInRecord);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        owned = dataInput.readLong();
        rented = dataInput.readLong();
        population = dataInput.readLong();
        logicalRecordPartNumber = dataInput.readLong();
        totalNumberOfPartsInRecord = dataInput.readLong();
        total = owned + rented;
    }
}
