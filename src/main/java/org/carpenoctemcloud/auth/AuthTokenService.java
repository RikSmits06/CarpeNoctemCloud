package org.carpenoctemcloud.auth;

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
}
