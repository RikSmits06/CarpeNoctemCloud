package org.carpenoctemcloud.delete_task_log;

import java.sql.Timestamp;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

/**
 * Database service which will create logs of the cleanup task.
 */
@Service
public class DeleteTaskLogService {
    private final NamedParameterJdbcTemplate template;

    /**
     * Initiates the service.
     *
     * @param template The template needed for querying the database.
     */
    public DeleteTaskLogService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Adds a new log to the database.
     *
     * @param start        The time when the task started.
     * @param end          The end of the task.
     * @param filesDeleted How many files where deleted.
     */
    public void addDeleteTaskLog(Timestamp start, Timestamp end, long filesDeleted) {
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("start", start).addValue("end", end)
                        .addValue("filesDeleted", filesDeleted);
        template.update(
                "insert into delete_task_log(started, ended, files_deleted) values (:start, :end, :filesDeleted);",
                source);
    }
}
