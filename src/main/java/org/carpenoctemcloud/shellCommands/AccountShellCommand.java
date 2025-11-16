package org.carpenoctemcloud.shellCommands;

import org.carpenoctemcloud.account.AccountService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class AccountShellCommand {
    private final AccountService accountService;

    public AccountShellCommand(AccountService accountService) {
        this.accountService = accountService;
    }

    @ShellMethod(key = "createAccount", value = "Create a new account in the database.")
    public String createAccount(@ShellOption(value = "The name of the new user.") String name,
                                @ShellOption(value = "The email of the user.") String email,
                                @ShellOption(value = "The password of the user.") String password,
                                @ShellOption(value = "True for admin, false for user.",
                                        defaultValue = "false") boolean isAdmin) {
        try {
            int id = accountService.createAccount(name, email, password, isAdmin);
            accountService.activateAccount(id);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Created account.";
    }
}
