package org.carpenoctemcloud.shell_commands;

import java.time.Duration;
import java.time.LocalDateTime;
import org.carpenoctemcloud.directory.DirectoryService;
import org.carpenoctemcloud.indexing.IndexingListener;
import org.carpenoctemcloud.indexing.ServerIndexer;
import org.carpenoctemcloud.indexing_listeners.IndexingListenerBatch;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.carpenoctemcloud.smb.ServerIndexerSMB;
import org.springframework.format.annotation.DurationFormat;
import org.springframework.format.datetime.standard.DurationFormatterUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Utility class for commands to scan servers manually.
 */
@ShellComponent
public class ScanServerCommand {
    private final RemoteFileService fileService;
    private final DirectoryService directoryService;

    /**
     * Creates a new object to store the shell command definitions.
     *
     * @param fileService The service used to store files.
     */
    public ScanServerCommand(RemoteFileService fileService, DirectoryService directoryService) {
        this.fileService = fileService;
        this.directoryService = directoryService;
    }

    /**
     * Shell command to scan a specific server so we can refresh one server.
     *
     * @param url The url of the smb server.
     * @return The duration of the scanning as a string.
     */
    @ShellMethod(key = "scanSMB", value = "Scans an SMB server.")
    public String ScanSMB(@ShellOption(value = "Url of the server to index.") String url) {
        ServerIndexer indexer = new ServerIndexerSMB(url);
        IndexingListener listener = new IndexingListenerBatch(fileService, directoryService);
        return timedIndexing(url, indexer, listener);
    }

    private String timedIndexing(String url, ServerIndexer indexer, IndexingListener listener) {
        LocalDateTime start = LocalDateTime.now();
        indexer.indexServer(listener);
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        return "Finished indexing " + listener.getTotalFilesIndexed() + " files of " + url +
                " in " + DurationFormatterUtils.print(duration, DurationFormat.Style.COMPOSITE) +
                ".";
    }
}
