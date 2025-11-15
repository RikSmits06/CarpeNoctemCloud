package org.carpenoctemcloud.remote_file;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a result set onto a total files count.
 */
public class TotalFilesMapper implements RowMapper<Integer> {

    /**
     * Default constructor, does not provide functionality.
     */
    public TotalFilesMapper() {
    }

    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("count");
    }
}
