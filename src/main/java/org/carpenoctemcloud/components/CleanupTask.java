package org.carpenoctemcloud.components;

import java.sql.Timestamp;
import java.time.Instant;
import org.carpenoctemcloud.remoteFile.RemoteFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.carpenoctemcloud.configuration.ConfigurationConstants.CLEANUP_RATE_IN_HOURS;
import static org.carpenoctemcloud.configuration.ConfigurationConstants.MAX_AGE_CACHE_IN_DAYS;

@Component
public class CleanupTask {
    private final RemoteFileService fileService;
    Logger logger = LoggerFactory.getLogger(CleanupTask.class);

    public CleanupTask(RemoteFileService fileService) {
        this.fileService = fileService;
    }

    @Scheduled(fixedDelay = CLEANUP_RATE_IN_HOURS * 60 * 60 * 1000)
    public void cleanupOldFiles() {
        logger.info("Started cleanup of old files.");
        Timestamp cutOff =
                Timestamp.from(Instant.now().minusSeconds(MAX_AGE_CACHE_IN_DAYS * 24 * 60 * 60));
        int deleteCount = fileService.deleteOldRemoteFiles(cutOff);
        logger.info("Finished cleaning up the old files. Deleted " + deleteCount + " files.");
    }
}
