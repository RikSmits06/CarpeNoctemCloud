package org.carpenoctemcloud.components;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import org.carpenoctemcloud.indexing.IndexingListener;
import org.carpenoctemcloud.indexing.IndexingListenerImpl;
import org.carpenoctemcloud.indexing.ServerIndexer;
import org.carpenoctemcloud.indexing.ServerIndexerSMB;
import org.carpenoctemcloud.remoteFile.RemoteFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DurationFormat;
import org.springframework.format.datetime.standard.DurationFormatterUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.carpenoctemcloud.configuration.ConfigurationConstants.INDEXING_RATE_IN_HOURS;

@Component
public class IndexingTask {
    private final Logger logger = LoggerFactory.getLogger(IndexingTask.class);

    private final RemoteFileService remoteFileService;

    public IndexingTask(RemoteFileService remoteFileService) {
        this.remoteFileService = remoteFileService;
    }


    /**
     * Indexes all servers every 2 hours,
     * with an initial delay(20 minutes) to prevent that testing will keep requesting from the servers.
     */
    @Scheduled(initialDelay = 20 * 60 * 1000, fixedDelay = INDEXING_RATE_IN_HOURS * 60 * 60 * 1000)
    public void indexAllServers() {
        IndexingListener listener = new IndexingListenerImpl(remoteFileService);

        String[] smbServers = {"spitfire.student.utwente.nl", "stroopwafel.student.utwente.nl",
                "univac.student.utwente.nl", "campuslaan53.student.utwente.nl"};

        Timestamp startTime = Timestamp.from(Instant.now());

        for (String smbServer : smbServers) {
            logger.info("Indexing SMB server: \"{}\".", smbServer);
            ServerIndexer indexer = new ServerIndexerSMB(smbServer);
            indexer.indexServer(listener);
            logger.info("Finished indexing the SMB server: \"{}\"", smbServer);
        }

        Timestamp endTime = Timestamp.from(Instant.now());
        Duration duration =
                Duration.between(startTime.toLocalDateTime(), endTime.toLocalDateTime());
        String durationFormatted =
                DurationFormatterUtils.print(duration, DurationFormat.Style.COMPOSITE);
        logger.info("Finished indexing in {}.", durationFormatted);
    }
}
