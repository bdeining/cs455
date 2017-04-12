package reducers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import records.AverageRoomRecord;

public class AverageRoomPercentileReducer extends Reducer<Text, AverageRoomRecord, Text, Text> {

    private long percent;

    @Override
    protected void reduce(Text key, Iterable<AverageRoomRecord> values, Context context)
            throws IOException, InterruptedException {
        List<Long> averages = new ArrayList<>();

        for (AverageRoomRecord val : values) {
            averages.add(getAverage(val.getRoomCounts()));
        }

        Collections.sort(averages);

        percent = find95thPercentile(averages);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        context.write(new Text("95th Percentile"), new Text(percent + " rooms"));
    }

    public long getAverage(long[] roomCounts) {
        long totalHouses = 0;
        long totalRooms = 0;
        for (int i = 0; i < roomCounts.length; i++) {
            long numberOfHousesWithSize = roomCounts[i];
            totalHouses += numberOfHousesWithSize;
            for (int c = 0; c < numberOfHousesWithSize; c++) {
                totalRooms += i + 1;
            }
        }
        return totalRooms / totalHouses;
    }

    public long find95thPercentile(List<Long> averages) {
        double roomCount95thPercentile = averages.size() * 0.95 - 1;
        int index = (int) roomCount95thPercentile;
        return averages.get(index);
    }
}
