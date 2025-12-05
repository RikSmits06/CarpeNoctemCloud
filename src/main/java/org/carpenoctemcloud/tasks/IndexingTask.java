package org.carpenoctemcloud.tasks;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import org.carpenoctemcloud.directory.DirectoryService;
import org.carpenoctemcloud.index_task_log.IndexTaskLogService;
import org.carpenoctemcloud.indexing.IndexingListener;
import org.carpenoctemcloud.indexing.ServerIndexer;
import org.carpenoctemcloud.indexing_listeners.IndexingListenerBatch;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.carpenoctemcloud.server.Server;
import org.carpenoctemcloud.server.ServerIndexerFactory;
import org.carpenoctemcloud.server.ServerProtocolNotFoundException;
import org.carpenoctemcloud.server.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Class which embodies the indexing task.
 * It runs periodically to make sure the database is up to date.
 */
@Component
public class IndexingTask {
    private static final Logger logger = LoggerFactory.getLogger(IndexingTask.class);
    private final RemoteFileService remoteFileService;
    private final IndexTaskLogService logService;
    private final DirectoryService directoryService;
    private final ServerService serverService;

    /**
     * Constructor for the indexing task, Requires remoteFileService to save the indexed files.
     *
     * @param remoteFileService The remoteFileService handling db requests.
     * @param logService        The log service used to store the indexing task result.
     * @param directoryService  The service given to the index listeners.
     * @param serverService     The service to get server information.
     */
    public IndexingTask(RemoteFileService remoteFileService, IndexTaskLogService logService,
                        DirectoryService directoryService, ServerService serverService) {
        this.remoteFileService = remoteFileService;
        this.logService = logService;
        this.directoryService = directoryService;
        this.serverService = serverService;
    }


    /**
     * Indexes all the servers every so often to keep the cache up to date.
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void indexAllServers() {
        IndexingListener listener = new IndexingListenerBatch(remoteFileService, directoryService);

        List<Server> servers = serverService.getServers();

        Timestamp startTime = Timestamp.from(Instant.now());

        for (Server server : servers) {
            try {
                ServerIndexer indexer = ServerIndexerFactory.getIndexer(server);
                indexer.indexServer(listener);
            } catch (ServerProtocolNotFoundException | RuntimeException e) {
                logger.error(e.getMessage());
            }
        }

        Timestamp endTime = Timestamp.from(Instant.now());
        logService.addIndexLog(startTime, endTime, listener.getTotalFilesIndexed());
    }
}
