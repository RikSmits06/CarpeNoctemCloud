package org.carpenoctemcloud.tasks;

import java.sql.Timestamp;
import java.time.Instant;
import org.carpenoctemcloud.delete_task_log.DeleteTaskLogService;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.carpenoctemcloud.configuration.ConfigurationConstants.MAX_AGE_CACHE_IN_DAYS;

/**
 * The Purpose of this class is to clean up old indexed files which haven't been found in the past few days.
 */
@Component
public class CleanupTask {
    private final DeleteTaskLogService logService;
    private final RemoteFileService fileService;

    /**
     * Constructor of the CleanupTask. It requires the RemoteFileService to delete old files.
     *
     * @param fileService The RemoteFileService which will be able to delete old files.
     */
    public CleanupTask(DeleteTaskLogService logService, RemoteFileService fileService) {
        this.logService = logService;
        this.fileService = fileService;
    }

    /**
     * Deletes old files periodically.
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupOldFiles() {
        Timestamp start = Timestamp.from(Instant.now());
        Timestamp cutOff =
                Timestamp.from(Instant.now().minusSeconds(MAX_AGE_CACHE_IN_DAYS * 24 * 60 * 60));
        int deleteCount = fileService.deleteOldRemoteFiles(cutOff);
        Timestamp end = Timestamp.from(Instant.now());
        logService.addDeleteTaskLog(start, end, deleteCount);
    }
}
