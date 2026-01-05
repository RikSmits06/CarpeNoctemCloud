package org.carpenoctemcloud.request_logging;

import org.carpenoctemcloud.auth.CurrentUserContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service responsible for interacting with the request_log table in the database.
 */
@Service
public class RequestLogService {
    private final NamedParameterJdbcTemplate template;

    /**
     * Creates a new service to interact with the database.
     *
     * @param template The template used to write queries.
     */
    public RequestLogService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Async function to save an endpoint hit.
     *
     * @param endpoint The endpoint which was hit.
     */
    @Async
    public void incrementRequestCounter(String endpoint) {
        SqlParameterSource source = new MapSqlParameterSource().addValue("endpoint", endpoint);
        template.update("""
                                insert into request_log(day, endpoint, count)
                                values (current_date, :endpoint, 1)
                                on conflict (day, endpoint) do update set count = request_log.count + 1;
                                """, source);

    }
}
