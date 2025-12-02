package org.carpenoctemcloud.directory;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

/**
 * Service to create and interact with the directory table.
 */
@Service
public class DirectoryService {
    private final NamedParameterJdbcTemplate template;

    /**
     * Creates a new directory service.
     *
     * @param template Template used to create queries for the database.
     */
    public DirectoryService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Creates directory in the database.
     *
     * @param serverName The name of the server, as in ftp.server.utwente for instance.
     * @param path       The path relative to the top level directory.
     */
    public void addDirectory(String serverName, String path) {
        MapSqlParameterSource source =
                new MapSqlParameterSource().addValue("serverName", serverName)
                        .addValue("path", path);
        template.update("""
                                insert into directory(path, server_id)
                                select :path, (select s.id from server s where s.host=:serverName)
                                on conflict do nothing;
                                """, source);
    }

    public Optional<Directory> getDirectory(long id) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("id", id);
        List<Directory> directory = template.query("""
                                                           select * from directory
                                                           where id=:id limit 1;
                                                           """, source, new DirectoryMapper());
        if (directory.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(directory.getFirst());
    }
}
