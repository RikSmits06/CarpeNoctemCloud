package org.carpenoctemcloud.remote_file_system;

import java.util.List;
import java.util.Optional;
import org.carpenoctemcloud.directory.Directory;
import org.carpenoctemcloud.directory.DirectoryMapper;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileMapper;
import org.carpenoctemcloud.server.Server;
import org.carpenoctemcloud.server.ServerMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class RemoteFileSystemService {
    private final NamedParameterJdbcTemplate template;

    public RemoteFileSystemService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Directory> getSubDirectories(long directoryID) {
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("directoryID", directoryID);
        return template.query("""
                                      select * from directory
                                               where directory.parent_directory=:directoryID;
                                      """, source, new DirectoryMapper());
    }

    public List<Directory> getTopLevelDirectories(long serverID) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("serverID", serverID);
        return template.query("""
                                      select * from directory
                                      where server_id=:serverID
                                      and parent_directory IS NULL;
                                      """, source, new DirectoryMapper());
    }

    public List<RemoteFile> getRemoteFilesInDirectory(long directoryID) {
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("directoryID", directoryID);
        return template.query("""
                                      select * from remote_file
                                               where remote_file.directory_id=:directoryID;
                                      """, source, new RemoteFileMapper());
    }
}
