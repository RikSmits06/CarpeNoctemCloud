package org.carpenoctemcloud.auth;

import java.util.Objects;
import java.util.Optional;
import org.carpenoctemcloud.account.Account;

/**
 * Context which holds the current user.
 */
public class CurrentUserContext {
    private Account user;

    /**
     * Creates a new context. Will default to not having a user.
     */
    public CurrentUserContext() {
        user = null;
    }

    /**
     * Checks whether there is a user in the context.
     *
     * @return True if a user exists, false otherwise.
     */
    public boolean userExists() {
        return !Objects.isNull(user);
    }

    /**
     * Gets the user from the context.
     *
     * @return An optional with the account or an empty optional if userExists is false.
     */
    public Optional<Account> getUser() {
        return Optional.ofNullable(user);
    }

    /**
     * Sets the user of the context.
     *
     * @param account The account of the user to pin to the current context.
     */
    public void setUser(Account account) {
        user = account;
    }
}
