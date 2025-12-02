package org.carpenoctemcloud.account;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service to create and view accounts.
 */
@Service
public class AccountService {
    private final NamedParameterJdbcTemplate template;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new AccountService.
     *
     * @param template        The template to interact with the database.
     * @param passwordEncoder The function used to encode passwords to hashes.
     */
    public AccountService(NamedParameterJdbcTemplate template, PasswordEncoder passwordEncoder) {
        this.template = template;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new account in the database. If it already exists, it will throw a runtime error.
     *
     * @param name     The name of the user to create, does not have to be unique!
     * @param email    The email of the user. Has to be unique.
     * @param password The password in plaintext of the user.
     * @param isAdmin  True if the user should be admin.
     * @return The id of the account if everything went well.
     */
    public int createAccount(String name, String email, String password, boolean isAdmin) {
        password = passwordEncoder.encode(password);
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("name", name).addValue("email", email)
                        .addValue("password", password).addValue("isAdmin", isAdmin);


        return template.query("""
                                      insert into account (name, email, password, is_admin)
                                      values (:name, :email, :password, :isAdmin)
                                      returning id as account_id;""", source, new AccountIdMapper())
                .getFirst();
    }

    /**
     * Activates the account of the given id.
     *
     * @param accountID The id of the account to activate.
     */
    public void activateAccount(int accountID) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("accountID", accountID);
        template.update("update account set email_confirmed=true where id=:accountID", source);
    }

    /**
     * Retrieves an account by email and also makes sure that this account is deactivated.
     *
     * @param email The email of the account.
     * @return Optional containing the account or an empty optional if the account does not exist or is not activated.
     */
    public Optional<Account> getActivatedAccountByEmail(String email) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("email", email);
        List<Account> accounts = template.query("""
                                                        select * from account where email=:email and email_confirmed limit 1;""",
                                                source, new AccountMapper());
        if (accounts.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(accounts.getFirst());
    }

    public Optional<Account> getAccountByEmail(String email) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("email", email);
        List<Account> accounts = template.query("""
                                                        select * from account where email=:email limit 1;""",
                                                source, new AccountMapper());
        if (accounts.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(accounts.getFirst());
    }
}
