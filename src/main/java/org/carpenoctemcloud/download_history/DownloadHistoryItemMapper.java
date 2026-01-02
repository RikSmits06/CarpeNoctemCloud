package org.carpenoctemcloud.download_history;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DownloadHistoryItemMapper implements RowMapper<DownloadHistoryItem> {
    @Nullable
    @Override
    public DownloadHistoryItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DownloadHistoryItem(rs.getInt("account_id"),
                rs.getInt("remote_file_id"),
                rs.getTimestamp("time"));
    }
}
