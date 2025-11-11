package org.carpenoctemcloud.delete_task_log;

import java.sql.Timestamp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class DeleteTaskLogService {
    private final JdbcTemplate template;

    public DeleteTaskLogService(JdbcTemplate template) {
        this.template = template;
    }

    public void addDeleteTaskLog(Timestamp start, Timestamp end, long filesDeleted) {
        SqlParameterSource source =
                new MapSqlParameterSource().addValue("start", start).addValue("end", end)
                        .addValue("filesDeleted", filesDeleted);
        template.update("insert into delete_task_log(started, ended, files_deleted) values (:start, :end, :filesDeleted);", source);
    }
}
