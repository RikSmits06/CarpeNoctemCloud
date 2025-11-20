package org.carpenoctemcloud.indexing_listeners;

import org.carpenoctemcloud.directory.DirectoryService;
import org.carpenoctemcloud.indexing.IndexedFile;
import org.carpenoctemcloud.indexing.IndexingListener;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Prints out all the indexed files to standard out.
 */
public class IndexingListenerImpl extends IndexingListener {

    private final RemoteFileService fileService;
    private final DirectoryService directoryService;
    private final Logger logger = LoggerFactory.getLogger(IndexingListenerImpl.class);

    /**
     * Constructor of the IndexingListenerImpl. Requires RemoteFileService to save indexed files.
     *
     * @param fileService The RemoteFileService.
     */
    public IndexingListenerImpl(RemoteFileService fileService, DirectoryService directoryService) {
        this.fileService = fileService;
        this.directoryService = directoryService;
    }

    @Override
    public void onNewFileIndexed(IndexedFile file) {
        fileService.addRemoteFile(file);
    }

    @Override
    protected void onDirectoryIndexed(String serverName, String path) {
        directoryService.addDirectory(serverName, path);
    }

    @Override
    public void onErrorWhileIndexing(Exception exception) {
        logger.warn(exception.getMessage());
    }

    /**
     * When the ServerIndexer has no more files to index, this can be called.
     */
    @Override
    public void onIndexingComplete() {

    }
}
