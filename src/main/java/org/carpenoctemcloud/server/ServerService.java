package org.carpenoctemcloud.server;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

/**
 * Service used to retrieve information about servers which have/will be indexed.
 */
@Service
public class ServerService {
    private final NamedParameterJdbcTemplate template;

    /**
     * Creates a new server service.
     *
     * @param template The template to query the data source.
     */
    public ServerService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Gets a list of all the servers.
     *
     * @return The list of all servers.
     */
    public List<Server> getServers() {
        return template.query("""
                                      select * from server;
                                      """, new ServerMapper());
    }

    /**
     * Searches for a server matching an ID.
     *
     * @param id The id to match.
     * @return An optional containing the server, is empty if no server has been found.
     */
    public Optional<Server> getServerByID(long id) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("serverID", id);
        List<Server> serverList = template.query("""
                                                         select * from server where server.id=:serverID limit 1;
                                                         """, source, new ServerMapper());

        if (serverList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(serverList.getFirst());
    }

    /**
     * Gets a serer based on the host name.
     *
     * @param host The name of the host. For instance: spitfire.student.utwente.nl.
     * @return The server wrapped in an optional.
     */
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
