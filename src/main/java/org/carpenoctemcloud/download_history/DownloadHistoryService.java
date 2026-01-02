package org.carpenoctemcloud.download_history;

import org.carpenoctemcloud.configuration.ConfigurationConstants;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DownloadHistoryService {
    private final NamedParameterJdbcTemplate template;

    public DownloadHistoryService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public void addFileToHistory(Integer accountID, int fileID) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("accountID", accountID).addValue("fileID", fileID);

        template.update("""
                insert into download_history(account_id, remote_file_id) values (:accountID, :fileID);
                """, source);
    }

    public List<RemoteFile> getRecentHistory(int accountID) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("accountID", accountID)
                .addValue("limit", ConfigurationConstants.MAX_FETCH_SIZE_HISTORY);
        return template.query("""
                select * from download_history
                         where account_id = :accountID
                order by time desc limit :limit;
                """, source, new RemoteFileMapper());
    }
}
