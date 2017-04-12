package util;

public class RecordParsingUtils {

    public static final String SUMMARY_LEVEL = "100";

    public static String getState(String unparsedText) {
        return unparsedText.substring(8, 10);
    }

    public static String getSummaryLevel(String unparsedText) {
        return unparsedText.substring(10, 13);
    }

    public static Long getLogicalRecordPartNumber(String unparsedText) {
        return Long.parseLong(unparsedText.substring(24, 28));
    }

    public static Long getTotalNumberOfPartsInRecord(String unparsedText) {
        return Long.parseLong(unparsedText.substring(28, 32));
    }

    /**
     * Parse data from mapper from start to end and convert it into a long.
     *
     * @param start        - start index in the unparsedText string
     * @param end          - end index in the unparsedText string
     * @param unparsedText - raw data from mapper
     * @return - parsed long
     */
    public static long parseTextIntoLong(int start, int end, String unparsedText) {
        long result = 0L;
        for (int i = start; i < end; i += 9) {
            Long reading = Long.parseLong(unparsedText.substring(i, i + 9));
            result += reading;
        }
        return result;
    }
}
