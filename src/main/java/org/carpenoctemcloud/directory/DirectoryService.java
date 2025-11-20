package org.carpenoctemcloud.directory;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DirectoryService {
    private final NamedParameterJdbcTemplate template;

    public DirectoryService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

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
}
