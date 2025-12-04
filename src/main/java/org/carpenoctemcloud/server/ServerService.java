package org.carpenoctemcloud.server;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class ServerService {
    private final NamedParameterJdbcTemplate template;

    public ServerService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Server> getServers() {
        return template.query("""
                                      select * from server;
                                      """, new ServerMapper());
    }

    public Optional<Server> getServerByID(long id) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("serverID", id);
        List<Server> serverList = template.query("""
                                                         select * from server where server.id=:serverID;
                                                         """, source, new ServerMapper());

        if (serverList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(serverList.getFirst());
    }

    public Optional<Server> getServerByHost(String host) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("host", host);
        List<Server> serverList = template.query("""
                                                         select * from server where server.host=:host;
                                                         """, source, new ServerMapper());

        if (serverList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(serverList.getFirst());
    }
}
