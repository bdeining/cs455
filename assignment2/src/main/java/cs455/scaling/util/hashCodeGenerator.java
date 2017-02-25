package cs455.scaling.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCodeGenerator {
    private static final String ALGORITHM = "SHA1";

    public static String generateHashCodeFromBytes(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        byte[] hash = digest.digest(data);
        BigInteger hashInt = new BigInteger(1, hash);

        return hashInt.toString(16);
    }
}