package org.carpenoctemcloud.directory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;

public class DirectoryMapper implements RowMapper<Directory> {
    @Override
    public Directory mapRow(ResultSet rs, int rowNum) throws SQLException {
        long parentDirectory = rs.getLong("parent_directory");
        boolean parentDirectoryWasNull = rs.wasNull();
        Optional<Long> parentDirectoryOpt =
                parentDirectoryWasNull ? Optional.empty() : Optional.of(parentDirectory);
        String path = rs.getString("path");
        String[] pathSplit = path.split("/");
        String name = pathSplit[pathSplit.length - 1];
        return new Directory(rs.getLong("id"), path, name, parentDirectoryOpt, rs.getInt("server_id"));
    }
}
