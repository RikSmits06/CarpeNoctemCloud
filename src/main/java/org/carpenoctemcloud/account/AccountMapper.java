package org.carpenoctemcloud.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a resultset to a {@link Account}.
 */
public class AccountMapper implements RowMapper<Account> {

    /**
     * Creates a new AccountMapper.
     */
    public AccountMapper() {
    }

    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Account(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                           rs.getBoolean("email_confirmed"), rs.getBoolean("is_admin"),
                           rs.getString("password"));
    }
}
