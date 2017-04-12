package records;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class HousingTypeRecord implements Writable {

    private long rural = 0;

    private long urban = 0;

    public HousingTypeRecord() {

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
        return getHousingString();
    }

    private String getHousingString() {
        return String.format("%s\t%s", rural, urban);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(rural);
        dataOutput.writeLong(urban);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        rural = dataInput.readLong();
        urban = dataInput.readLong();
    }
}
