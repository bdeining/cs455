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
}
