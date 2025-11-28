package org.carpenoctemcloud.auth;

import java.util.Objects;
import java.util.Optional;
import org.carpenoctemcloud.account.Account;

public class CurrentUserContext {
    private Account user;

    public CurrentUserContext() {
        user = null;
    }

    public boolean userExists() {
        return !Objects.isNull(user);
    }

    public Optional<Account> getUser() {
        return Optional.ofNullable(user);
    }

    public void setUser(Account account) {
        user = account;
    }
}
