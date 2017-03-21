package wordcount;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Mapper: Reads line by line, split them into words. Emit <word, 1> pairs.
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, Tenure> {

    /*
    The logical record number appears in position 19 (the starting index for a record is 1, not 0) of each segment.
    Following this, beginning in positions 25 and 29, are the logical record part number and the total number of parts in the record. By viewing these two fields together, the sequence of the segment and the total number of segments can be quickly determined.
    For example, 1 in the logical record part number and 2 in the total number of parts in record field indicates that this is segment 1 of the 2 segments that comprise the logical record.

You should pay attention to the record part number field when trying to extract a particular field using
the boundaries provided below, because a particular field appears in either segment 1 or 2.
Boundaries in the following table are defined with respect to the starting position of the segment.
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String unparsedText = value.toString();
        String summaryLevel = getSummaryLevel(unparsedText);
        if (summaryLevel.equals("100")) {

            String state = getState(unparsedText);
            Long logicalRecordPartNumber = getLogicalRecordPartNumber(unparsedText);
            Long totalNumberOfPartsInRecord = getTotalNumberOfPartsInRecord(unparsedText);
            // Segment 1
            if (!logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                Tenure tenure = new Tenure(getRenterOccupied(unparsedText), getOwnerOccupied(unparsedText), getPopulation(unparsedText));
                tenure.setLogicalRecordPartNumber(logicalRecordPartNumber);
                tenure.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
                context.write(new Text(state), tenure);
            }

        }
    }

    public String getState(String unparsedText) {
        return unparsedText.substring(8,10);
    }

    public String getSummaryLevel(String unparsedText) {
        return unparsedText.substring(10,13);
    }

    public Long getLogicalRecordNumber(String unparsedText) {
        Long logicalRecordNumber = Long.parseLong(unparsedText.substring(18,24));
        return logicalRecordNumber;
    }

    public Long getLogicalRecordPartNumber(String unparsedText) {
        Long logicalRecordPartNumber = Long.parseLong(unparsedText.substring(24,28));
        return logicalRecordPartNumber;
    }

    public Long getTotalNumberOfPartsInRecord(String unparsedText) {
        Long totalNumberOfPartsInRecord = Long.parseLong(unparsedText.substring(28, 32));
        return totalNumberOfPartsInRecord;
    }

    public Long getOwnerOccupied(String unparsedText) {
        Long ownerOccupied = Long.parseLong(unparsedText.substring(1803, 1812));
        return ownerOccupied;
    }

    public Long getRenterOccupied(String unparsedText) {
        Long renterOccupied = Long.parseLong(unparsedText.substring(1812, 1821));
        return renterOccupied;
    }

    public Long getPopulation(String unparsedText) {
        Long population = Long.parseLong(unparsedText.substring(300, 309));
        return population;
    }
}
