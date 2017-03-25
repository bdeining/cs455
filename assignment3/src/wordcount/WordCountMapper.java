package wordcount;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Mapper: Reads line by line, split them into words. Emit <word, 1> pairs.
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, Tenure> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String unparsedText = value.toString();
        String summaryLevel = getSummaryLevel(unparsedText);
        if (summaryLevel.equals("100")) {

            String state = getState(unparsedText);
            Long logicalRecordPartNumber = getLogicalRecordPartNumber(unparsedText);
            Long totalNumberOfPartsInRecord = getTotalNumberOfPartsInRecord(unparsedText);

            Tenure tenure = new Tenure();

            // Segment 1
            if (!logicalRecordPartNumber.equals(totalNumberOfPartsInRecord)) {
                tenure.setPopulation(getPopulation(unparsedText));
                tenure.setMaleNeverMarried(getMaleNeverMarried(unparsedText));
                tenure.setFemaleNeverMarried(getFemaleNeverMarried(unparsedText));

                tenure.setHispanic0to18(getHispanicBelow18(unparsedText));
                tenure.setHispanic19to29(getHispanic19to29(unparsedText));
                tenure.setHispanic30to39(getHispanic30to39(unparsedText));

                tenure.setLogicalRecordPartNumber(logicalRecordPartNumber);
                tenure.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
                // Segment 2
            } else {
                tenure.setRented(getRenterOccupied(unparsedText));
                tenure.setOwned(getOwnerOccupied(unparsedText));
                tenure.setUrban(getUrbanOccupied(unparsedText));
                tenure.setRural(getRuralOccupied(unparsedText));
                tenure.setMedianHousingValue(medianHouseValue(unparsedText));
            }
            tenure.setType(context.getConfiguration()
                    .get("type"));
            tenure.setLogicalRecordPartNumber(logicalRecordPartNumber);
            tenure.setTotalNumberOfPartsInRecord(totalNumberOfPartsInRecord);
            context.write(new Text(state), tenure);
        }
    }

    public String getState(String unparsedText) {
        return unparsedText.substring(8, 10);
    }

    public String getSummaryLevel(String unparsedText) {
        return unparsedText.substring(10, 13);
    }

    public Long getLogicalRecordNumber(String unparsedText) {
        Long logicalRecordNumber = Long.parseLong(unparsedText.substring(18, 24));
        return logicalRecordNumber;
    }

    public Long getLogicalRecordPartNumber(String unparsedText) {
        Long logicalRecordPartNumber = Long.parseLong(unparsedText.substring(24, 28));
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

    public Long getMaleNeverMarried(String unparsedText) {
        Long maleNeverMarried = Long.parseLong(unparsedText.substring(4422, 4431));
        return maleNeverMarried;
    }

    public Long getFemaleNeverMarried(String unparsedText) {
        Long femaleNeverMarried = Long.parseLong(unparsedText.substring(4467, 4476));
        return femaleNeverMarried;
    }

    /*
    (a).Percentage of people below 18 years (inclusive) old.
    /* 3864 - 3972, 4144 - 4252 */
    public Long getHispanicBelow18(String unparsedText) {
        Long hispanicBelow18 = 0L;

        /* Male */
        for (int i = 3864; i < 3973; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanicBelow18 += reading;
        }

        /* Female */
        for (int i = 4144; i < 4253; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanicBelow18 += reading;
        }

        return hispanicBelow18;
    }

    /* 3981 - 4017, 4261 - 4298
    *  (b).Percentage of people between 19 (inclusive) and 29 (inclusive) years old.
    * */
    public Long getHispanic19to29(String unparsedText) {
        Long hispanic19to29 = 0L;

        /* Male */
        for (int i = 3981; i < 4018; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanic19to29 += reading;
        }

        /* Female */
        for (int i = 4261; i < 4298; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanic19to29 += reading;
        }

        return hispanic19to29;
    }

    /*
            4026 - 4035
     *     (c). Percentage of people between 30 (inclusive) and 39 (inclusive) years old.
     */

    public Long getHispanic30to39(String unparsedText) {
        Long hispanic30to39 = 0L;

        /* Male */
        for (int i = 4026; i < 4036; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanic30to39 += reading;
        }

        /* Female */
        for (int i = 4306; i < 4316; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            hispanic30to39 += reading;
        }

        return hispanic30to39;
    }

    public Long getUrbanOccupied(String unparsedText) {
        Long urban = 0L;
        urban += Long.parseLong(unparsedText.substring(1821, 1830));
        urban += Long.parseLong(unparsedText.substring(1830, 1839));
        return urban;
    }

    public Long getRuralOccupied(String unparsedText) {
        Long rural = Long.parseLong(unparsedText.substring(1839, 1848));
        return rural;
    }

    public String medianHouseValue(String unparsedText) {
        List<String> strings = Arrays.asList("Less than $15,000",
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

        long total = 0L;
        Map<String, Long> longList = new HashMap<>();

        int count = 0;
        for (int i = 2928; i < 3099; i += 9) {
            long number = Long.parseLong(unparsedText.substring(i, i + 9));
            longList.put(strings.get(count), number);
            total += number;
            count++;
        }

        long bracket = 0L;
        long median = total / 2;

        for (Map.Entry<String, Long> entry : longList.entrySet()) {
            bracket += entry.getValue();
            if (bracket >= median) {
                return entry.getKey();
            }

        }

        return strings.get(strings.size() - 1);
    }
}
