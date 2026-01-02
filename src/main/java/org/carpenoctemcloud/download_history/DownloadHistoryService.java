package org.carpenoctemcloud.download_history;

import org.carpenoctemcloud.configuration.ConfigurationConstants;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service used to interact with the download history.
 */
@Service
public class DownloadHistoryService {
    private final NamedParameterJdbcTemplate template;

    /**
     * Creates a new DownloadHistoryService.
     *
     * @param template The template used to interact with the database.
     */
    public DownloadHistoryService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Adds a file to the history of downloaded files.
     *
     * @param accountID      The id of the account that downloaded a file. This can be null if user was not logged in.
     * @param fileID         The id of the file that was downloaded.
     * @param redirectorName The name of the redirector that was used to create the redirect file for research purposes.
     */
    public void addFileToHistory(Integer accountID, int fileID, String redirectorName) {
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("accountID", accountID)
                .addValue("fileID", fileID)
                .addValue("redirectorName", redirectorName);

        template.update("""
                insert into download_history(account_id, remote_file_id, redirector_used) 
                values (:accountID, :fileID, :redirectorName);
                """, source);
    }

    /**
     * Returns the n last {@link RemoteFile} which the account has viewed where n is defined in {@link ConfigurationConstants}.
     *
     * @param accountID The id of the account to retrieve the history of.
     * @return The list of history items.
     */
    public List<RemoteFile> getRecentHistory(int accountID) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("accountID", accountID)
                .addValue("limit", ConfigurationConstants.MAX_FETCH_SIZE_HISTORY);
        return template.query("""
                select rf.*
                from download_history dh,
                     remote_file rf
                where dh.account_id = :accountID
                  and rf.id = dh.remote_file_id
                order by time desc
                limit :limit;
                """, source, new RemoteFileMapper());
    }
}
