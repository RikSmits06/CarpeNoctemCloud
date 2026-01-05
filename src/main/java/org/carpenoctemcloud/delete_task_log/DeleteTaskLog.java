package org.carpenoctemcloud.delete_task_log;

import java.sql.Timestamp;

/**
 * Entity to store in the DB when a new {@link org.carpenoctemcloud.tasks.CleanupTask} has been completed.
 *
 * @param id           The id of the task log.
 * @param started      When the task was started.
 * @param ended        When the task ended.
 * @param filesDeleted How many files where deleted.
 */
public record DeleteTaskLog(int id, Timestamp started, Timestamp ended, long filesDeleted) {
}
