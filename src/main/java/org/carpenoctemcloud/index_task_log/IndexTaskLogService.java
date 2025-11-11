package org.carpenoctemcloud.index_task_log;

import java.sql.Timestamp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class IndexTaskLogService {
    private final JdbcTemplate template;

    public IndexTaskLogService(JdbcTemplate template) {
        this.template = template;
    }

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
