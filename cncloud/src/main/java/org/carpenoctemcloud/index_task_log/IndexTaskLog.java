package org.carpenoctemcloud.index_task_log;

import java.sql.Timestamp;

/**
 * Entity used for storing indexing logs.
 *
 * @param id           The id of the log.
 * @param started      The time the indexing started.
 * @param ended        THe time the indexing ended.
 * @param filesIndexed The amount of files that where indexed.
 */
public record IndexTaskLog(int id, Timestamp started, Timestamp ended, long filesIndexed) {
}
