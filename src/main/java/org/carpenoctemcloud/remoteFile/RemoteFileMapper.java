package org.carpenoctemcloud.remoteFile;


import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class RemoteFileMapper implements RowMapper<RemoteFile> {
    @Override
    public RemoteFile mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new RemoteFile(resultSet.getLong("id"), resultSet.getString("name"),
                              resultSet.getString("download_url"),
                              resultSet.getTimestamp("last_indexed"));
    }
}
