package org.carpenoctemcloud.auth;

import java.util.List;
import java.util.Optional;
import org.carpenoctemcloud.account.Account;
import org.carpenoctemcloud.account.AccountMapper;
import org.carpenoctemcloud.account.AccountService;
import org.carpenoctemcloud.configuration.AuthConfiguration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for the auth_token table in the database.
 */
@Service
public class AuthTokenService {
    private final NamedParameterJdbcTemplate template;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new service with the required dependencies.
     *
     * @param template        The template to interact with the database.
     * @param accountService  Account service is used to get the account linked with the token.
     * @param passwordEncoder The encoder used to create password hashes or compare them.
     */
    public AuthTokenService(NamedParameterJdbcTemplate template, AccountService accountService,
                            PasswordEncoder passwordEncoder) {
        this.template = template;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves an account from the database to which the token belongs.
     * Will log the time the token was used.
     *
     * @param token The token which to check with.
     * @return The account or an empty optional if the token does not exist.
     */
    public Optional<Account> accountFromToken(String token) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("token", token);
        List<Account> account = template.query("""
                                                       with acc as (select a.*
                                                                    from account a,
                                                                         auth_token t
                                                                    where t.token = :token
                                                                      and t.account_id = a.id
                                                                      and t.expiry > now()
                                                                    limit 1)
                                                       update auth_token
                                                       set last_accessed=now()
                                                       from acc
                                                       where token = :token
                                                       returning acc.*;
                                                       """, source, new AccountMapper());
        if (account.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(account.getFirst());
    }

    /**
     * Deletes the tokens of an account.
     *
     * @param accountID ID of the account to delete the tokens of.
     */
    public void deleteTokensOfAccount(int accountID) {
        SqlParameterSource source = new MapSqlParameterSource("accountID", accountID);
        template.update("""
                                delete from auth_token
                                where account_id=:accountID;
                                """, source);
    }

    /**
     * Adds a token to the database if the credentials are valid.
     *
     * @param email    The email of the user.
     * @param password The raw password of the user.
     * @return An empty is returned if login did not happen otherwise an optional with the token.
     */
    public Optional<String> addAuthTokenByCredentials(String email, String password) {
        Optional<Account> accountOpt = accountService.getActivatedAccountByEmail(email);

        if (accountOpt.isEmpty()) {
            return Optional.empty();
        }
        Account account = accountOpt.get();

        if (!passwordEncoder.matches(password, account.encodedPassword())) {
            return Optional.empty();
        }

        String token = AuthUtil.randomToken(AuthConfiguration.AUTH_TOKEN_LENGTH);
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("email", email).addValue("token", token)
                        .addValue("expireHours", AuthConfiguration.MAX_AGE_AUTH_TOKEN_IN_HOURS);
        template.update("""
                                with found_account as
                                         (select id from account where email = :email and email_confirmed)
                                insert
                                into auth_token (token, account_id, expiry)
                                select :token, found_account.id, now() + interval '1 hour' * :expireHours
                                from found_account;
                                """, source);
        return Optional.of(token);
    }

    /**
     * Deletes all tokens which have expired.
     */
    public void deleteOldTokens() {
        template.update("""
                                delete from auth_token
                                where expiry <= now();
                                """, new MapSqlParameterSource());
    }
}
