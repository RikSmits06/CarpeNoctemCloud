package org.carpenoctemcloud.indexing;

/**
 * Interface for a class which will get all files from a server.
 */
public interface ServerIndexer {
    /**
     * Indexes a server and passes all files and errors to the listener.
     *
     * @param listener The listener which receives the files and errors.
     */
    void indexServer(IndexingListener listener);
}
