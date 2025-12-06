package org.carpenoctemcloud.shell_commands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import org.carpenoctemcloud.directory.DirectoryService;
import org.carpenoctemcloud.indexing.IndexingListener;
import org.carpenoctemcloud.indexing.ServerIndexer;
import org.carpenoctemcloud.indexing_listeners.IndexingListenerBatch;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.carpenoctemcloud.server.Server;
import org.carpenoctemcloud.server.ServerIndexerFactory;
import org.carpenoctemcloud.server.ServerProtocolNotFoundException;
import org.carpenoctemcloud.server.ServerService;
import org.carpenoctemcloud.tasks.IndexingTask;
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
    private final IndexingTask indexingTask;
    private final ServerService serverService;

    /**
     * Creates a new object to store the shell command definitions.
     *
     * @param fileService      The service used to store files.
     * @param directoryService Service given to the listeners to create directories.
     * @param indexingTask     The task used to index servers.
     * @param serverService    Service used to get all servers.
     */
    public ScanServerCommand(RemoteFileService fileService, DirectoryService directoryService,
                             IndexingTask indexingTask, ServerService serverService) {
        this.fileService = fileService;
        this.directoryService = directoryService;
        this.indexingTask = indexingTask;
        this.serverService = serverService;
    }

    /**
     * Shell command to scan a specific server so we can refresh one server.
     *
     * @param url The url of the smb server.
     * @return The duration of the scanning as a string.
     */
    @ShellMethod(key = "scan", value = "Scans a server.")
    public String scan(@ShellOption(value = "Url of the server to index.") String url) {
        Optional<Server> serverOpt = serverService.getServerByHost(url);
        if (serverOpt.isEmpty()) {
            return "Server does not exist in database.";
        }
        Server server = serverOpt.get();
        ServerIndexer indexer = null;
        try {
            indexer = ServerIndexerFactory.getIndexer(server);
        } catch (ServerProtocolNotFoundException e) {
            return "Could not get indexer for given server.";
        }
        IndexingListener listener = new IndexingListenerBatch(fileService, directoryService);
        return timedIndexing(url, indexer, listener);
    }

    /**
     * Scans all known smb servers manually.
     */
    @ShellMethod(key = "scanAll", value = "Scans all servers in the database.")
    public void scanAll() {
        indexingTask.indexAllServers();
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
