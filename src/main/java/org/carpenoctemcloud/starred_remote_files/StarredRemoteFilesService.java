package org.carpenoctemcloud.starred_remote_files;

import java.util.List;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

/**
 * Database service to star, unstar and get starred files.
 */
@Service
public class StarredRemoteFilesService {
    private final NamedParameterJdbcTemplate template;

    /**
     * Creates a new service.
     *
     * @param template The template of the database.
     */
    public StarredRemoteFilesService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Gets all files the user has starred.
     *
     * @param accountID The id of the account.
     * @return The list of starred files.
     */
    public List<RemoteFile> getStarredFiles(int accountID) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("account", accountID);
        return template.query("""
                                      select rf.*
                                      from starred_remote_files starred,
                                           remote_file rf
                                      where rf.id = starred.remote_file_id
                                      and starred.account_id=:account;
                                      """, source, new RemoteFileMapper());
    }

    /**
     * Adds a star to a file.
     *
     * @param accountID The id of the account.
     * @param fileID    The id of the file to check.
     */
    public void starFile(int accountID, int fileID) {
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("account", accountID).addValue("file", fileID);
        template.update("""
                                insert into starred_remote_files(account_id, remote_file_id)
                                values (:account, :file);
                                """, source);
    }

    /**
     * Removes star from a file.
     *
     * @param accountID The account.
     * @param fileID    The file to unstar.
     */
    public void unStarFile(int accountID, int fileID) {
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("account", accountID).addValue("file", fileID);
        template.update("""
                                delete from starred_remote_files where account_id=:account
                                                                   and remote_file_id=:file;
                                """, source);
    }
}
