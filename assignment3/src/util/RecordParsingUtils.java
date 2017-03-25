package util;

public class RecordParsingUtils {

    public static String getState(String unparsedText) {
        return unparsedText.substring(8, 10);
    }

    public static String getSummaryLevel(String unparsedText) {
        return unparsedText.substring(10, 13);
    }

    public static Long getLogicalRecordNumber(String unparsedText) {
        Long logicalRecordNumber = Long.parseLong(unparsedText.substring(18, 24));
        return logicalRecordNumber;
    }

    public static Long getLogicalRecordPartNumber(String unparsedText) {
        Long logicalRecordPartNumber = Long.parseLong(unparsedText.substring(24, 28));
        return logicalRecordPartNumber;
    }

    public static Long getTotalNumberOfPartsInRecord(String unparsedText) {
        Long totalNumberOfPartsInRecord = Long.parseLong(unparsedText.substring(28, 32));
        return totalNumberOfPartsInRecord;
    }

}
