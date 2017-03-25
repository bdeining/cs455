package cs455;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Before;
import org.junit.Test;

import wordcount.WordCountMapper;

public class TestMapper {

    private static final String TEST_STRING = "________AZ100_____12345622141123";

    private WordCountMapper wordCountMapper;

    @Before
    public void setUp() {
        wordCountMapper = new WordCountMapper();
    }

    @Test
    public void testGetState() {
        String state = wordCountMapper.getState(TEST_STRING);
        assertThat(state, is("AZ"));
    }

    @Test
    public void testGetSummaryLevel() {
        String summaryLevel = wordCountMapper.getSummaryLevel(TEST_STRING);
        assertThat(summaryLevel, is("100"));
    }
/*

    @Test
    public void testGetLogicalRecordNumber() {
        String logicalRecordNumber = wordCountMapper.getLogicalRecordNumber(TEST_STRING);
        assertThat(logicalRecordNumber.length(), is(6));
        assertThat(logicalRecordNumber, is("123456"));
    }

    @Test
    public void testGetLogicalRecordPartNumber() {
        String logicalRecordPartNumber = wordCountMapper.getLogicalRecordPartNumber(TEST_STRING);
        assertThat(logicalRecordPartNumber.length(), is(4));
        assertThat(logicalRecordPartNumber, is("2214"));
    }

    @Test
    public void testGetTotalNumberOfPartsInRecord() {
        String totalNumberOfPartsInRecord = wordCountMapper.getTotalNumberOfPartsInRecord(
                TEST_STRING);
        assertThat(totalNumberOfPartsInRecord.length(), is(4));
        assertThat(totalNumberOfPartsInRecord, is("1123"));
    }
*/

    @Test
    public void testPercentage() {
        double one = 100L;
        double two = 21L;
        System.out.println(two / one);

    }

    @Test
    public void test() {
        int count = 0;
        for (int i=3864; i<3973; i+=9) {
            count++;
        }

        assertThat(count ,is(13));

        count = 0;
        for (int i=4144; i<4253; i+=9) {
            count++;
        }

        assertThat(count ,is(13));

        count = 0;
        for (int i=3981; i<4018; i+=9) {
            count++;
        }

        assertThat(count ,is(5));

        count = 0;
        for (int i=4261; i<4298; i+=9) {
            count++;
        }

        assertThat(count ,is(5));


        count = 0;
        for (int i=4026; i<4036; i+=9) {
            count++;
        }

        assertThat(count ,is(2));

    }

    @Test
    public void test2() {
        System.out.println(67/2);
    }
}
