package record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Record implements Writable {

    private long logicalRecordPartNumber = 0;

    private long totalNumberOfPartsInRecord = 0;

    public Record() {

    }

    public void setLogicalRecordPartNumber(long logicalRecordPartNumber) {
        this.logicalRecordPartNumber = logicalRecordPartNumber;
    }

    public long getLogicalRecordPartNumber() {
        return logicalRecordPartNumber;
    }

    public void setTotalNumberOfPartsInRecord(long totalNumberOfPartsInRecord) {
        this.totalNumberOfPartsInRecord = totalNumberOfPartsInRecord;
    }

    public long getTotalNumberOfPartsInRecord() {
        return totalNumberOfPartsInRecord;
    }


    @Override
    public void write(DataOutput dataOutput) throws IOException {

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {

    }

}
