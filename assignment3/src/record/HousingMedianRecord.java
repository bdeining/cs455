package record;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.Writable;

public class HousingMedianRecord implements Writable {

    public static final List<String> VALUE_LIST = Arrays.asList("Less than $15,000",
            "$15,000 - $19,999",
            "$20,000 - $24,999",
            "$25,000 - $29,999",
            "$30,000 - $34,999",
            "$35,000 - $39,999",
            "$40,000 - $44,999",
            "$45,000 - $49,999",
            "$50,000 - $59,999",
            "$60,000 - $74,999",
            "$75,000 - $99,999",
            "$100,000 - $124,999",
            "$125,000 - $149,999",
            "$150,000 - $174,999",
            "$175,000 - $199,999",
            "$200,000 - $249,999",
            "$250,000 - $299,999",
            "$300,000 - $399,999",
            "$400,000 - $499,999",
            "$500,000 or more");

    private Map<String, Long> map;

    public HousingMedianRecord() {
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
