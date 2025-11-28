package org.carpenoctemcloud.auth;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import org.carpenoctemcloud.account.Account;
import org.carpenoctemcloud.account.AccountMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
class AuthTokenService {
    private final NamedParameterJdbcTemplate template;

    public AuthTokenService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public Optional<Account> accountFromToken(String token) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("token", token);
        List<Account> account = template.query("""
                                                       select a.*
                                                       from account a, auth_token t
                                                       where t.token=:token
                                                         and t.account_id=a.id
                                                         and t.expiry > now()
                                                       limit 1;
                                                       """, source, new AccountMapper());
        if (account.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(account.getFirst());
    }

    public String addAuthTokenByEmail(String email) {
        String token = randomToken();
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("email", email).addValue("token", token);
        template.update("""
                                with found_account_id as
                                         (select id from account where email = :email and email_confirmed)
                                insert
                                into auth_token (token, account_id, expiry)
                                values (:token, found_account_id, now());
                                """, source);
        return token;
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
