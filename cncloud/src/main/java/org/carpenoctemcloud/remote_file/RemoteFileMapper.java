package org.carpenoctemcloud.remote_file;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a result of a query into a RemoteFile.
 */
public class RemoteFileMapper implements RowMapper<RemoteFile> {

    /**
     * Default constructor, does not provide functionality.
     */
    public RemoteFileMapper() {
    }

    @Override
    public RemoteFile mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new RemoteFile(resultSet.getLong("id"), resultSet.getString("name"),
                              resultSet.getLong("directory_id"),
                              resultSet.getTimestamp("last_indexed"), Optional.ofNullable(
                resultSet.getObject("category_id", Integer.class)));
    }
}
