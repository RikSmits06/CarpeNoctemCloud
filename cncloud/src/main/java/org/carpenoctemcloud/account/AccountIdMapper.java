package org.carpenoctemcloud.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row to an account ID as its first column.
 */
public class AccountIdMapper implements RowMapper<Integer> {

    /**
     * Creates a new AccountIdMapper.
     */
    public AccountIdMapper() {
    }

    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt(1);
    }
}
