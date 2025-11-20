package org.carpenoctemcloud.tasks;

import java.sql.Timestamp;
import java.time.Instant;
import org.carpenoctemcloud.directory.DirectoryService;
import org.carpenoctemcloud.index_task_log.IndexTaskLogService;
import org.carpenoctemcloud.indexing.IndexingListener;
import org.carpenoctemcloud.indexing.ServerIndexer;
import org.carpenoctemcloud.indexing_listeners.IndexingListenerBatch;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.carpenoctemcloud.smb.ServerIndexerSMB;
import org.carpenoctemcloud.smb.SmbConstants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Class which embodies the indexing task.
 * It runs periodically to make sure the database is up to date.
 */
@Component
public class IndexingTask {
    private final RemoteFileService remoteFileService;
    private final IndexTaskLogService logService;
    private final DirectoryService directoryService;

    /**
     * Constructor for the indexing task, Requires remoteFileService to save the indexed files.
     *
     * @param remoteFileService The remoteFileService handling db requests.
     * @param logService        The log service used to store the indexing task result.
     */
    public IndexingTask(RemoteFileService remoteFileService, IndexTaskLogService logService,
                        DirectoryService directoryService) {
        this.remoteFileService = remoteFileService;
        this.logService = logService;
        this.directoryService = directoryService;
    }


    /**
     * Indexes all the servers every so often to keep the cache up to date.
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void indexAllServers() {
        IndexingListener listener = new IndexingListenerBatch(remoteFileService, directoryService);

        Timestamp startTime = Timestamp.from(Instant.now());

        for (String smbServer : SmbConstants.SMB_SERVERS) {
            ServerIndexer indexer = new ServerIndexerSMB(smbServer);
            indexer.indexServer(listener);
        }

        Timestamp endTime = Timestamp.from(Instant.now());
        logService.addIndexLog(startTime, endTime, listener.getTotalFilesIndexed());
    }
}
