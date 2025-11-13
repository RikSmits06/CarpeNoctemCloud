package org.carpenoctemcloud.account;

import java.security.SecureRandom;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final NamedParameterJdbcTemplate template;

    public AccountService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public int createAccount(String name, String email, String password, boolean isAdmin) {
        String salt = createSalt();
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("name", name).addValue("email", email)
                        .addValue("password", password).addValue("salt", salt)
                        .addValue("isAdmin", isAdmin);


        return template.query("""
                                      insert into account (name, email, password, salt, is_admin)
                                      values (:name, :email, sha256(cast(:password || '|' || :salt as bytea)), :salt, :isAdmin)
                                      returning id as account_id;""", source, new AccountIdMapper())
                .getFirst();
    }

    public void activateAccount(int accountID) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("accountID", accountID);
        template.update("update account set email_confirmed=true where id=:accountID", source);
    }

    private String createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[100];
        random.nextBytes(bytes);
        return bytesToHex(bytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
