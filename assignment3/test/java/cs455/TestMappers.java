package cs455;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import records.HousingMedianRecord;
import reducers.AverageRoomPercentileReducer;

public class TestMappers {

    @Test
    public void testMarriageBoundaries() {
        int start = 4422;
        int end = 4431;
        int count = 0;
        for (int i = start; i < end; i += 9) {
            count++;
        }
        assertThat(count, is(1));
    }

    @Test
    public void testHispanicBoundaries() {
        int count = 0;
        for (int i = 3864; i < 3973; i += 9) {
            count++;
        }

        assertThat(count, is(13));

        count = 0;
        for (int i = 4143; i < 4252; i += 9) {
            count++;
        }

        assertThat(count, is(13));

        count = 0;
        for (int i = 3981; i < 4018; i += 9) {
            count++;
        }

        assertThat(count, is(5));

        count = 0;
        for (int i = 4260; i < 4297; i += 9) {
            count++;
        }

        assertThat(count, is(5));

        count = 0;
        for (int i = 4026; i < 4036; i += 9) {
            count++;

        }

        assertThat(count, is(2));

        count = 0;
        for (int i = 4305; i < 4315; i += 9) {
            count++;
        }

        assertThat(count, is(2));

        count = 0;
        for (int i = 3864; i < 4135; i += 9) {
            System.out.println(i + "-" + (i + 9));
            count++;
        }

        assertThat(count, is(31));

        count = 0;
        for (int i = 4143; i < 4414; i += 9) {
            System.out.println(i + "-" + (i + 9));
            count++;
        }

        assertThat(count, is(31));

        //4143, 4414
        //3864, 4135
    }

    @Test
    public void testMedianHousingValue() {
        int count = 0;
        for (int i = 2928; i < 3100; i += 9) {
            count++;
        }

        assertThat(count, is(HousingMedianRecord.VALUE_LIST.size()));
    }

    @Test
    public void testHousingType() {
        int start = 1821;
        int end = 1831;
        int count = 0;
        for (int i = start; i < end; i += 9) {
            count++;
        }

        assertThat(count, is(2));

        count = 0;
        start = 1839;
        end = 1848;

        for (int i = start; i < end; i += 9) {
            count++;
        }

        assertThat(count, is(1));
    }

    @Test
    public void testDoublePercentage() {
        double totalPopulation = 12937926;
        double elderlyPopulation = 210110;

        double percentage = elderlyPopulation / totalPopulation * 100;

        System.out.println(percentage);

    }

    @Test
    public void testHouseRoomPercentage() {
        long[] houseRoom = {10560, 28870, 87742, 186260, 240665, 195648, 127018, 86260, 81089};
        long result = new AverageRoomPercentileReducer().getAverage(houseRoom);
        System.out.println(result);
    }

    @Test
    public void testPercentile() {
        List<Long> longList = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            longList.add((long) i);
        }
        long percentile = new AverageRoomPercentileReducer().find95thPercentile(longList);
        System.out.println(percentile);

    }
}
