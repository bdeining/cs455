package record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class HispanicRecord extends Record {
    private long hispanic0to18 = 0;

    private long hispanic19to29 = 0;

    private long hispanic30to39 = 0;

    public HispanicRecord() {

    }

    public void setHispanic0to18(long hispanic0to18) {
        this.hispanic0to18 = hispanic0to18;
    }

    public long getHispanic0to18() {
        return hispanic0to18;
    }

    public void setHispanic19to29(long hispanic19to29) {
        this.hispanic19to29 = hispanic19to29;
    }

    public long getHispanic19to29() {
        return hispanic19to29;
    }

    public void setHispanic30to39(long hispanic30to39) {
        this.hispanic30to39 = hispanic30to39;
    }

    public long getHispanic30to39() {
        return hispanic30to39;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        super.write(dataOutput);
        dataOutput.writeLong(hispanic0to18);
        dataOutput.writeLong(hispanic19to29);
        dataOutput.writeLong(hispanic30to39);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        super.readFields(dataInput);
        hispanic0to18 = dataInput.readLong();
        hispanic19to29 = dataInput.readLong();
        hispanic30to39 = dataInput.readLong();
    }

    @Override
    public String toString() {
        return getHispanicString();
    }

    private String getHispanicString() {
        if (getHispanic30to39() == 0 || getHispanic19to29() == 0 || getHispanic0to18() == 0) {
            return "";
        }

        double totalHispanic = getHispanic0to18() + getHispanic19to29() + getHispanic30to39();
        double below18Percentage = getHispanic0to18() / totalHispanic * 100;
        double b19to29Percentage = getHispanic19to29() / totalHispanic * 100;
        double b30to39Percentage = getHispanic30to39() / totalHispanic * 100;
        return String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s",
                totalHispanic,
                getHispanic0to18(),
                below18Percentage,
                getHispanic19to29(),
                b19to29Percentage,
                getHispanic30to39(),
                b30to39Percentage);

    }
}
