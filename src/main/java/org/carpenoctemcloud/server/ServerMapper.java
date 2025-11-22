package org.carpenoctemcloud.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Used to map a resultset to a Server entity.
 */
public class ServerMapper implements RowMapper<Server> {

    /**
     * Creates a new ServerMapper.
     */
    public ServerMapper() {

    }

    @Override
    public Server mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Server(rs.getInt("id"), rs.getString("host"), rs.getString("protocol"),
                          rs.getString("download_prefix"));
    }
}
