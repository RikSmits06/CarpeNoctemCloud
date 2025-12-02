package org.carpenoctemcloud.auth;

import java.security.SecureRandom;

public class AuthUtil {
    private AuthUtil() {
    }

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
