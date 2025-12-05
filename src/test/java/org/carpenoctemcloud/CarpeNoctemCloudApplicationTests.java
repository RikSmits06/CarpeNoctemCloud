package org.carpenoctemcloud;

import org.carpenoctemcloud.account.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@CNCloudTestNoDatabase
class CarpeNoctemCloudApplicationTests {

    @Autowired
    private AccountService accountService;

    /**
     * Test is supposed to be empty to test the loading of the apps' context.
     */
    @Test
    @SuppressWarnings("empty")
    void contextLoads() {
    }
}
