package org.carpenoctemcloud.request_logging;

import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for interacting with the request_log table in the database.
 */
@Service
public class UserDownloadLogService {
    private final NamedParameterJdbcTemplate template;

    /**
     * Creates a new service to interact with the database.
     *
     * @param template The template used to write queries.
     */
    public UserDownloadLogService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     *  function to save an endpoint hit.
     *
     * @param userId The endpoint which was hit.
     */
    public List<RemoteFile> getRecentDownloads(int userId) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("userId", userId);
        return template.query("""
                select * from remote_file where id in (select remote_file_id from user_download_log where uid=:userId order by day desc limit 10);
                """, source, new RemoteFileMapper());

    }
}
