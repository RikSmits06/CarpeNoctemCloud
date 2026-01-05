package org.carpenoctemcloud.indexing;

/**
 * Interface for classes which receive the files from indexing.
 */
public abstract class IndexingListener {
    private long totalFilesIndexed = 0;

    /**
     * Initiates a new listener.
     */
    public IndexingListener() {
    }

    /**
     * Function which is called when a new file is indexed.
     *
     * @param file The file which was indexed by the ServerIndexer.
     */
    abstract protected void onNewFileIndexed(IndexedFile file);

    /**
     * Function which is called when a new directory is found.
     *
     * @param serverName The host name of the server.
     * @param path       The path to the directory.
     */
    abstract protected void onDirectoryIndexed(String serverName, String path);

    /**
     * When the ServerIndexer has an error, the error will be given to the listener. For logging purposes.
     * The ServerIndexer will terminate or continue by itself.
     *
     * @param exception The exception which was thrown while indexing or connecting to the server.
     */
    abstract protected void onErrorWhileIndexing(Exception exception);

    /**
     * When the ServerIndexer has no more files to index, this can be called.
     */
    abstract protected void onIndexingComplete();

    /**
     * Calls the onNewFileIndexed event for the listener and makes sure other things are done as well.
     *
     * @param file The file that got indexed.
     */
    final public void fireNewFileIndexedEvent(IndexedFile file) {
        this.totalFilesIndexed++;
        this.onNewFileIndexed(file);
    }

    /**
     * Fires the NewDirectoryEvent for when a new directory is discovered.
     *
     * @param serverName The name of the server to fire it for.
     * @param path       The path to the folder.
     */
    final public void fireNewDirectoryEvent(String serverName, String path) {
        this.onDirectoryIndexed(serverName, path);
    }

    /**
     * Fires the onErrorEvent event to the listener.
     *
     * @param exception The error that occurred.
     */
    final public void fireErrorEvent(Exception exception) {
        this.onErrorWhileIndexing(exception);
    }

    /**
     * Fires event which indicates that indexing is done.
     */
    final public void fireIndexingCompleteEvent() {
        this.onIndexingComplete();
    }

    /**
     * Gets the total amount of files that have been successfully indexed.
     *
     * @return The amount of files that have been indexed.
     */
    final public long getTotalFilesIndexed() {
        return totalFilesIndexed;
    }
}
