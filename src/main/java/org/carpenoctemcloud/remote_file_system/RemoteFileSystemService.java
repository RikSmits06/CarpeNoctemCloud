package org.carpenoctemcloud.remote_file_system;

import java.util.List;
import org.carpenoctemcloud.directory.Directory;
import org.carpenoctemcloud.directory.DirectoryMapper;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

/**
 * Service which queries the database to get information to reconstruct a file system.
 */
@Service
public class RemoteFileSystemService {
    private final NamedParameterJdbcTemplate template;

    /**
     * Creates a new service.
     *
     * @param template The template linked to the database.
     */
    public RemoteFileSystemService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Gets the directories which are the children of the given directory.
     *
     * @param directoryID The id of the parent.
     * @return A list of children.
     */
    public List<Directory> getSubDirectories(long directoryID) {
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("directoryID", directoryID);
        return template.query("""
                                      select * from directory
                                               where directory.parent_directory=:directoryID;
                                      """, source, new DirectoryMapper());
    }

    /**
     * The top level directory of a server.
     *
     * @param serverID The server to get the top level of.
     * @return List of directories which have no parent except the server itself.
     */
    public List<Directory> getTopLevelDirectories(long serverID) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("serverID", serverID);
        return template.query("""
                                      select * from directory
                                      where server_id=:serverID
                                      and parent_directory IS NULL;
                                      """, source, new DirectoryMapper());
    }

    /**
     * Retrieves files which are in a given directory.
     *
     * @param directoryID The id of the directory.
     * @return A list of the files which are children.
     */
    public List<RemoteFile> getRemoteFilesInDirectory(long directoryID) {
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("directoryID", directoryID);
        return template.query("""
                                      select * from remote_file
                                               where remote_file.directory_id=:directoryID;
                                      """, source, new RemoteFileMapper());
    }
}
