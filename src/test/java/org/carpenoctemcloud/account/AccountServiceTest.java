package org.carpenoctemcloud.account;

import java.util.Optional;
import org.carpenoctemcloud.CNCloudTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@CNCloudTest
class AccountServiceTest {


    @Autowired
    private AccountService accountService;

    @Test
    public void accountCreationTest() {
        String email = "test@student.utwente.nl";
        String name = "Ben Dover";
        int id = accountService.createAccount(name, email, "12345678", false);
        Optional<Account> accountOpt = accountService.getAccountByEmail(email);
        assertTrue(accountOpt.isPresent());
        Account account = accountOpt.get();
        assertEquals(id, account.id());
        assertEquals(email, account.email());
        assertFalse(account.emailConfirmed());
        assertFalse(account.isAdmin());
        assertEquals(name, account.name());

        accountService.activateAccount(id);
        accountOpt = accountService.getActivatedAccountByEmail(email);
        assertTrue(accountOpt.isPresent());
        account = accountOpt.get();
        assertTrue(account.emailConfirmed());
    }
}