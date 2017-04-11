package record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.Writable;

public class RentMedianRecord implements Writable {

    public static final List<String> VALUE_LIST = Arrays.asList("Less than $100",
            "$100 to $149",
            "$150 to $199",
            "$200 to $249",
            "$250 to $299",
            "$300 to $349",
            "$350 to $399",
            "$400 to $449",
            "$450 to $499",
            "$500 to $549",
            "$550 to $599",
            "$600 to $649",
            "$650 to $699",
            "$700 to $749",
            "$750 to $999",
            "$1000 or more",
            "No cash rent");

    private Map<String, Long> map;

    public RentMedianRecord() {
        createEmptyMap();
    }

    public Map<String, Long> getMap() {
        return map;
    }

    public void setMap(Map<String, Long> map) {
        this.map = map;
    }

    public void createEmptyMap() {
        map = new LinkedHashMap<>();
        for (String string : VALUE_LIST) {
            map.put(string, 0L);
        }
    }

    @Override
    public String toString() {
        return getHousingString();
    }

    private String getHousingString() {
        long total = 0;
        for (Long value : map.values()) {
            total += value;
        }

        long median = total / 2;
        long result = 0;

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            result += entry.getValue();
            if (result >= median) {
                return entry.getKey();
            }
        }

        return VALUE_LIST.get(VALUE_LIST.size() - 1);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        for (Long value : map.values()) {
            dataOutput.writeLong(value);
        }
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        for (String string : map.keySet()) {
            map.put(string, dataInput.readLong());
        }
    }
}
