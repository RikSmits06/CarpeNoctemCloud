package org.carpenoctemcloud.indexing;

import org.carpenoctemcloud.remoteFile.RemoteFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Prints out all the indexed files to standard out.
 */
public class IndexingListenerImpl implements IndexingListener {

    private final RemoteFileService fileService;
    private Logger logger = LoggerFactory.getLogger(IndexingListenerImpl.class);

    public IndexingListenerImpl(RemoteFileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void onNewFileIndexed(IndexedFile file) {
        fileService.addRemoteFile(file);
    }

    @Override
    public void OnErrorWhileIndexing(Exception exception) {
        logger.warn(exception.getMessage());
    }
}
