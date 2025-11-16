package org.carpenoctemcloud.index_task_log;

import java.sql.Timestamp;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

/**
 * Service meant to store the indexing tasks which have been completed.
 */
@Service
public class IndexTaskLogService {
    private final NamedParameterJdbcTemplate template;

    /**
     * Creates a new IndexTaskLogService.
     *
     * @param template The template used to query the database.
     */
    public IndexTaskLogService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Adds a new index log to the database.
     *
     * @param started      The time the task started.
     * @param ended        The time the task finished.
     * @param filesIndexed How many files where indexed.
     */
    public void addIndexLog(Timestamp started, Timestamp ended, long filesIndexed) {
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("started", started).addValue("ended", ended)
                        .addValue("filesIndexed", filesIndexed);
        template.update("""
                                insert into index_task_log(started, ended, files_indexed)
                                values (:started, :ended, :filesIndexed);
                                """, source);
    }
}
