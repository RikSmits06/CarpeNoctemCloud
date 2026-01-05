package org.carpenoctemcloud.shell_commands;

import org.carpenoctemcloud.account.AccountService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Class which contains commands to interact with the account service.
 */
@ShellComponent
public class AccountShellCommand {
    private final AccountService accountService;

    /**
     * Creates a new AccountShellCommand.
     *
     * @param accountService An account service used to create a new account.
     */
    public AccountShellCommand(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Creates a new account and activates it.
     *
     * @param name     The name of the new user (mandatory).
     * @param email    The email of the new user (mandatory).
     * @param password The password in plaintext of the new user (mandatory).
     * @param isAdmin  If the new user should be an admin (optional, false by default).
     * @return A string with the result of the method.
     */
    @ShellMethod(key = "createAccount", value = "Create a new account in the database.")
    public String createAccount(@ShellOption(value = "The name of the new user.") String name,
                                @ShellOption(value = "The email of the user.") String email,
                                @ShellOption(value = "The password of the user.") String password,
                                @ShellOption(value = "True for admin, false for user.",
                                        defaultValue = "false") boolean isAdmin) {
        try {
            int id = accountService.createAccount(name, email, password, isAdmin);
            accountService.activateAccount(id);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
        return "Created account.";
    }
}
