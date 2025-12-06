package org.carpenoctemcloud.auth;

import java.util.Optional;
import org.carpenoctemcloud.CNCloudTest;
import org.carpenoctemcloud.account.Account;
import org.carpenoctemcloud.account.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@CNCloudTest
class AuthTokenServiceTest {

    private final String email = "test@student.utwente.nl";
    private final String password = "12345678";
    private final String name = "Ben Dover";
    private int accountID;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthTokenService authTokenService;

    @BeforeEach
    public void setup() {
        accountID = accountService.createAccount(name, email, password, false);
        Optional<Account> accountOptional = accountService.getAccountByEmail(email);
        assertTrue(accountOptional.isPresent());
        Account account = accountOptional.get();
        assertEquals(email, account.email());
        assertEquals(name, account.name());
        assertFalse(account.emailConfirmed());
    }

    @Test
    public void validLoginTest() {
        // Activated accounts should get tokens.
        accountService.activateAccount(accountID);
        Optional<String> tokenOpt = authTokenService.addAuthTokenByCredentials(email, password);
        assertTrue(tokenOpt.isPresent());

        // Account from token should equal account by email.
        String token = tokenOpt.get();
        Optional<Account> accountFromToken = authTokenService.accountFromToken(token);
        Optional<Account> account = accountService.getActivatedAccountByEmail(email);
        assertTrue(accountFromToken.isPresent());
        assertTrue(account.isPresent());
        assertEquals(account.get(), accountFromToken.get());

        // Should be able to log out and not have a token no more.
        authTokenService.deleteTokensOfAccount(accountID);
        assertTrue(authTokenService.accountFromToken(token).isEmpty());
    }

    @Test
    public void invalidLoginTest() {
        // Account is not activated, should not receive token.
        Optional<String> tokenOpt = authTokenService.addAuthTokenByCredentials(email, password);
        assertTrue(tokenOpt.isEmpty());

        // No auth token for false credentials.
        accountService.activateAccount(accountID);
        tokenOpt = authTokenService.addAuthTokenByCredentials(email, "wrong password");
        assertTrue(tokenOpt.isEmpty());
        tokenOpt = authTokenService.addAuthTokenByCredentials("wrong@student.utwente.nl", password);
        assertTrue(tokenOpt.isEmpty());

        // Random token should not give us an account.
        String token = AuthUtil.randomToken(AuthConfiguration.AUTH_TOKEN_LENGTH);
        Optional<Account> accountOpt = authTokenService.accountFromToken(token);
        assertTrue(accountOpt.isEmpty());
    }
}