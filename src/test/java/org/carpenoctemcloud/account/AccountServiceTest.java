package org.carpenoctemcloud.account;

import java.util.Optional;
import org.carpenoctemcloud.CNCloudTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CNCloudTest
class AccountServiceTest {

    private final String email = "h.j.smits@student.utwente.nl";
    @Autowired
    private AccountService accountService;

    @Test
    public void accountCreationTest() {
        int id = accountService.createAccount("Rik Smits", email, "12345678", true);
        Optional<Account> accountOpt = accountService.getAccountByEmail(email);
        assertTrue(accountOpt.isPresent());
        Account account = accountOpt.get();
        assertEquals(id, account.id());
        assertEquals(email, account.email());
    }
}