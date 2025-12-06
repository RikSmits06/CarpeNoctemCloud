package org.carpenoctemcloud.account;

import java.util.Optional;
import org.carpenoctemcloud.CNCloudTest;
import org.junit.jupiter.api.Assertions;
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
        String password = "12345678";

        // Account creating should work.
        int id = accountService.createAccount(name, email, password, false);
        Optional<Account> accountOpt = accountService.getAccountByEmail(email);
        assertTrue(accountOpt.isPresent());

        // Of course, the info has to match input.
        Account account = accountOpt.get();
        assertEquals(id, account.id());
        assertEquals(email, account.email());
        assertFalse(account.emailConfirmed());
        assertFalse(account.isAdmin());
        assertEquals(name, account.name());
        assertNotEquals(password, account.encodedPassword());

        // Activating account should also work.
        accountService.activateAccount(id);
        accountOpt = accountService.getActivatedAccountByEmail(email);
        assertTrue(accountOpt.isPresent());
        account = accountOpt.get();
        assertTrue(account.emailConfirmed());
    }

    @Test
    public void incorrectAccountInformationTest() {
        // We should only be able to make 1 account per email.
        accountService.createAccount("test", "duplicate@student.utwente.nl", "12345678", false);

        Assertions.assertThrows(RuntimeException.class, () -> accountService.createAccount("test",
                                                                                           "duplicate@student.utwente.nl",
                                                                                           "12345678",
                                                                                           false));

        // Only student emails should work.
        Assertions.assertThrows(RuntimeException.class,
                                () -> accountService.createAccount("no student", "test@gmail.com",
                                                                   "12345678", true));

        // Non-existing emails should not return anything.
        assertTrue(accountService.getAccountByEmail("noexist@student.utwente.nl").isEmpty());
        assertTrue(accountService.getActivatedAccountByEmail("duplicate@student.utwente.nl")
                           .isEmpty());
    }
}