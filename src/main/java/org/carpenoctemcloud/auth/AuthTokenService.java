package org.carpenoctemcloud.auth;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import org.carpenoctemcloud.account.Account;
import org.carpenoctemcloud.account.AccountMapper;
import org.carpenoctemcloud.account.AccountService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {
    private final NamedParameterJdbcTemplate template;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public AuthTokenService(NamedParameterJdbcTemplate template, AccountService accountService,
                            PasswordEncoder passwordEncoder) {
        this.template = template;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

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

    public void deleteTokensOfAccount(int accountID) {
        SqlParameterSource source = new MapSqlParameterSource("accountID", accountID);
        template.update("""
                                delete from auth_token
                                where account_id=:accountID;
                                """, source);
    }

    /**
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

        String token = randomToken();
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("email", email).addValue("token", token)
                        .addValue("expireHours", AuthConfiguration.MAX_AGE_IN_HOURS);
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

    public void deleteOldTokens() {
        template.update("""
                                delete from auth_token
                                where expiry <= now();
                                """, new MapSqlParameterSource());
    }

    private String randomToken() {
        char[] charSet =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder ret = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 256; i++) {
            ret.append(charSet[random.nextInt(0, charSet.length)]);
        }

        return ret.toString();
    }
}
