package org.carpenoctemcloud.indexing_listeners;

import org.carpenoctemcloud.indexing.IndexedFile;
import org.carpenoctemcloud.indexing.IndexingListener;

/**
 * Debugging listener to print out the indexed files to stdout.
 */
public class IndexingListenerPrint extends IndexingListener {
    /**
     * Creates a new listener which prints out to stdout.
     */
    public IndexingListenerPrint() {
    }

    /**
     * Function which is called when a new file is indexed.
     *
     * @param file The file which was indexed by the ServerIndexer.
     */
    @Override
    protected void onNewFileIndexed(IndexedFile file) {
        System.out.println("Indexed " + file.filename() + " (" + file.host() + "): " + file.path());
    }

    @Override
    protected void onDirectoryIndexed(String serverName, String path) {
        System.out.println("Indexed directory " + path + " (" + serverName + ")");
    }

    /**
     * When the ServerIndexer has an error, the error will be given to the listener. For logging purposes.
     * The ServerIndexer will terminate or continue by itself.
     *
     * @param exception The exception which was thrown while indexing or connecting to the server.
     */
    @Override
    protected void onErrorWhileIndexing(Exception exception) {
        System.out.println("Error: " + exception.getMessage());
    }

    /**
     * When the ServerIndexer has no more files to index, this can be called.
     */
    @Override
    protected void onIndexingComplete() {
        System.out.println("Indexing completed.");
    }
}
