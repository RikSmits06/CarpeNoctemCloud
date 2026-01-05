package org.carpenoctemcloud.tasks;

import java.sql.Timestamp;
import java.time.Instant;
import org.carpenoctemcloud.auth.AuthTokenService;
import org.carpenoctemcloud.delete_task_log.DeleteTaskLogService;
import org.carpenoctemcloud.email_confirmation.EmailConfirmationTokenService;
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
    private final AuthTokenService authTokenService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;

    /**
     * Constructor of the CleanupTask. It requires the RemoteFileService to delete old files.
     *
     * @param fileService                   The RemoteFileService which will be able to delete old files.
     * @param logService                    The service which will add records of the cleanup tasks.
     * @param authTokenService              The service used to clean up  old auth tokens.
     * @param emailConfirmationTokenService Service used to clean up old email tokens.
     */
    public CleanupTask(DeleteTaskLogService logService, RemoteFileService fileService,
                       AuthTokenService authTokenService,
                       EmailConfirmationTokenService emailConfirmationTokenService) {
        this.logService = logService;
        this.fileService = fileService;
        this.authTokenService = authTokenService;
        this.emailConfirmationTokenService = emailConfirmationTokenService;
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

    /**
     * Deletes old auth tokens to avoid collisions.
     */
    @Scheduled(cron = "0 0/15 * * * *")
    public void cleanupOldAuthTokens() {
        authTokenService.deleteOldTokens();
    }

    /**
     * Cleans up all old email tokens.
     */
    @Scheduled(cron = "0 0/15 * * * *")
    public void cleanupEmailConfirmationTokens() {
        emailConfirmationTokenService.cleanupOldTokens();
    }
}
