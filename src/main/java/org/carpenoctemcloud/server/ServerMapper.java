package org.carpenoctemcloud.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ServerMapper implements RowMapper<Server> {
    @Override
    public Server mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Server(rs.getInt("id"), rs.getString("host"), rs.getString("protocol"),
                          rs.getString("download_prefix"));
    }
}
