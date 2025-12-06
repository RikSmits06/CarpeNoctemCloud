package org.carpenoctemcloud.auth;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthUtilTest {

    @Test
    public void authTokenGenTest() {
        int numberOfTests = 10_000;
        Random random = new Random();
        for (int i = 0; i < numberOfTests; i++) {
            int length = random.nextInt(1000);
            String token = AuthUtil.randomToken(length);
            assertEquals(length, token.length());
        }
    }

    /**
     * If we generate a big token and do this n times, they should all be different.
     * The chance that they are the same is so astronomically low that we can do this.
     */
    @Test
    public void authTokenUniqueTest() {
        Set<String> result = new HashSet<>();
        int numOfTokens = 20;
        int tokenLength = 200;

        for (int i = 0; i < numOfTokens; i++) {
            result.add(AuthUtil.randomToken(tokenLength));
        }

        assertEquals(numOfTokens, result.size());
    }
}