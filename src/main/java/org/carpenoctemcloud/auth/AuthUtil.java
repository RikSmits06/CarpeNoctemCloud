package org.carpenoctemcloud.auth;

import java.security.SecureRandom;

/**
 * Utility class to make authentication easier.
 */
public class AuthUtil {
    private AuthUtil() {
    }

    /**
     * Creates a random token using secure random. the token consists of base-62 chars.
     *
     * @param tokenLength The length of the token in bytes.
     * @return The token in string format.
     */
    public static String randomToken(int tokenLength) {
        char[] charSet =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder ret = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < tokenLength; i++) {
            ret.append(charSet[random.nextInt(0, charSet.length)]);
        }

        return ret.toString();
    }
}
