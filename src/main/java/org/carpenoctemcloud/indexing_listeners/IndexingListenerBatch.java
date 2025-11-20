package org.carpenoctemcloud.indexing_listeners;

import java.util.ArrayList;
import org.carpenoctemcloud.directory.DirectoryService;
import org.carpenoctemcloud.indexing.IndexedFile;
import org.carpenoctemcloud.indexing.IndexingListener;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link IndexingListener} which saves to the database using batches instead of 1 by 1.
 */
public class IndexingListenerBatch extends IndexingListener {
    private final Logger logger;
    private final int MAX_BUFFER_SIZE = 1000;
    private final ArrayList<IndexedFile> buffer;
    private final RemoteFileService fileService;
    private final DirectoryService directoryService;

    /**
     * Creates a new IndexingListenerBatch and instantiates the underlying buffer.
     *
     * @param fileService      The service to save the found files to.
     * @param directoryService The service used to create new directories.
     */
    public IndexingListenerBatch(RemoteFileService fileService, DirectoryService directoryService) {
        this.fileService = fileService;
        this.directoryService = directoryService;
        logger = LoggerFactory.getLogger(IndexingListenerBatch.class);
        buffer = new ArrayList<>(MAX_BUFFER_SIZE);
        buffer.ensureCapacity(MAX_BUFFER_SIZE);
    }

    /**
     * Function which is called when a new file is indexed.
     *
     * @param file The file which was indexed by the ServerIndexer.
     */
    @Override
    protected void onNewFileIndexed(IndexedFile file) {
        buffer.add(file);

        if (buffer.size() >= MAX_BUFFER_SIZE) {
            this.writeBuffer();
        }
    }

    @Override
    protected void onDirectoryIndexed(String serverName, String path) {
        directoryService.addDirectory(serverName, path);
    }

    /**
     * When the ServerIndexer has an error, the error will be given to the listener. For logging purposes.
     * The ServerIndexer will terminate or continue by itself.
     *
     * @param exception The exception which was thrown while indexing or connecting to the server.
     */
    @Override
    protected void onErrorWhileIndexing(Exception exception) {
        logger.warn(exception.getMessage());
    }

    /**
     * When the ServerIndexer has no more files to index, this can be called.
     */
    @Override
    protected void onIndexingComplete() {
        this.writeBuffer();
    }

    private void writeBuffer() {
        fileService.addRemoteFiles(buffer);
        buffer.clear();
    }
}