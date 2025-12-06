package org.carpenoctemcloud.auth;

import java.security.SecureRandom;

/**
 * Utility class to make authentication easier.
 */
public class AuthUtil {
    private final static char[] charSet =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();

    private AuthUtil() {
    }

    /**
     * Creates a random token using secure random. the token consists of base-62 chars.
     *
     * @param tokenLength The length of the token in bytes.
     * @return The token in string format.
     */
    public static String randomToken(int tokenLength) {
        StringBuilder ret = new StringBuilder();
        SecureRandom random = new SecureRandom();

        random.ints(tokenLength, 0, charSet.length).forEach(i -> {
            ret.append(charSet[i]);
        });

        return ret.toString();
    }
}
