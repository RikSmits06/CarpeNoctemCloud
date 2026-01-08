package org.carpenoctemcloud.tasks;

import org.carpenoctemcloud.index_task_log.IndexTaskLogService;
import org.carpenoctemcloud.indexing.ServerIndexer;
import org.carpenoctemcloud.indexing_listeners.IndexingListenerBatch;
import org.carpenoctemcloud.server.Server;
import org.carpenoctemcloud.server.ServerIndexerFactory;
import org.carpenoctemcloud.server.ServerProtocolNotFoundException;
import org.carpenoctemcloud.server.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Class which embodies the indexing task.
 * It runs periodically to make sure the database is up to date.
 */
@Component()
public class IndexingTask {
    private static final Logger logger = LoggerFactory.getLogger(IndexingTask.class);
    private final IndexTaskLogService logService;
    private final ServerService serverService;
    private final IndexingListenerBatch indexingListenerBatch;

    /**
     * Constructor for the indexing task, Requires remoteFileService to save the indexed files.
     *
     * @param logService    The log service used to store the indexing task result.
     * @param serverService The service to get server information.
     */
    public IndexingTask(IndexTaskLogService logService, ServerService serverService, IndexingListenerBatch indexingListenerBatch) {
        this.logService = logService;
        this.serverService = serverService;
        this.indexingListenerBatch = indexingListenerBatch;
    }


    /**
     * Indexes all the servers every so often to keep the cache up to date.
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void indexAllServers() {
        List<Server> servers = serverService.getServers();

        Timestamp startTime = Timestamp.from(Instant.now());

        for (Server server : servers) {
            try {
                ServerIndexer indexer = ServerIndexerFactory.getIndexer(server);
                indexer.indexServer(indexingListenerBatch);
            } catch (ServerProtocolNotFoundException | RuntimeException e) {
                logger.error(e.getMessage());
            }
        }

        Timestamp endTime = Timestamp.from(Instant.now());
        logService.addIndexLog(startTime, endTime, indexingListenerBatch.getTotalFilesIndexed());
        indexingListenerBatch.resetFilesIndexedCount();
    }
}
