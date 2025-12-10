package org.carpenoctemcloud.email_confirmation;

import java.util.Optional;
import org.carpenoctemcloud.CNCloudTest;
import org.carpenoctemcloud.account.Account;
import org.carpenoctemcloud.account.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CNCloudTest
class EmailConfirmationTokenServiceTest {
    private final String email = "test@student.utwente.nl";
    private Account account;
    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailConfirmationTokenService emailConfirmationTokenService;

    @BeforeEach
    public void setup() {
        accountService.createAccount("Test", email, "12345678", false);
        var accountOpt = accountService.getAccountByEmail(email);
        assertTrue(accountOpt.isPresent());
        account = accountOpt.get();
    }

    @Test
    public void emailActivationTest() {
        // Test the activation of an account by token.
        String token = emailConfirmationTokenService.generateConfirmationTokenByEmail(email);
        assertTrue(accountService.getActivatedAccountByEmail(email).isEmpty());
        emailConfirmationTokenService.activateEmailByToken(token);
        Optional<Account> activatedAccountOpt = accountService.getActivatedAccountByEmail(email);
        assertTrue(activatedAccountOpt.isPresent());
        Account activatedAccount = activatedAccountOpt.get();
        assertTrue(activatedAccount.emailConfirmed());

        // Activated account should be the same as non-activated account.
        assertEquals(account.id(), activatedAccount.id());
        assertEquals(account.email(), activatedAccount.email());
        assertEquals(account.isAdmin(), activatedAccount.isAdmin());
        assertEquals(account.encodedPassword(), activatedAccount.encodedPassword());
        assertEquals(account.name(), activatedAccount.name());
    }
}