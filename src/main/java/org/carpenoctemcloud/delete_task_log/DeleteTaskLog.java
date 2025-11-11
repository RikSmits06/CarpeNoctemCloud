package org.carpenoctemcloud.delete_task_log;

import java.sql.Timestamp;

public record DeleteTaskLog(int id, Timestamp started, Timestamp ended, long filesDeleted) {
}
