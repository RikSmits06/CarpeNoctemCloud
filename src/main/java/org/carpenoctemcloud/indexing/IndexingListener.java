package org.carpenoctemcloud.indexing;

/**
 * Interface for classes which receive the files from indexing.
 */
public interface IndexingListener {
    /**
     * Function which is called when a new file is indexed.
     *
     * @param file The file which was indexed by the ServerIndexer.
     */
    void onNewFileIndexed(IndexedFile file);

    /**
     * When the ServerIndexer has an error, the error will be given to the listener. For logging purposes.
     * The ServerIndexer will terminate or continue by itself.
     *
     * @param exception The exception which was thrown while indexing or connecting to the server.
     */
    void OnErrorWhileIndexing(Exception exception);

    /**
     * When the ServerIndexer has no more files to index, this can be called.
     */
    void onIndexingComplete();
}
