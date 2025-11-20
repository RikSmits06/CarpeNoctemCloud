package org.carpenoctemcloud.ftp;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.carpenoctemcloud.indexing.IndexedFile;
import org.carpenoctemcloud.indexing.IndexingListener;
import org.carpenoctemcloud.indexing.ServerIndexer;

public class ServerIndexerFTP implements ServerIndexer {
    private final String url;

    public ServerIndexerFTP(String url) {
        this.url = url;
    }

    /**
     * Indexes a server and passes all files and errors to the listener.
     *
     * @param listener The listener which receives the files and errors.
     */
    @Override
    public void indexServer(IndexingListener listener) {
        FTPClient client = new FTPClient();

        try {
            client.connect(url);
            client.enterLocalPassiveMode();
            client.login("anonymous", "CNCloud@");
        } catch (IOException e) {
            listener.fireErrorEvent(e);
            listener.fireIndexingCompleteEvent();
            return;
        }

        walkDir(client, listener, "/");
        listener.fireIndexingCompleteEvent();

        try {
            client.logout();
            client.disconnect();
        } catch (Exception ignored) {
        }
    }

    private void walkDir(FTPClient client, IndexingListener listener, String dir) {
        try {
            client.changeWorkingDirectory(dir);
            for (FTPFile file : client.listFiles()) {
                if (file.isDirectory()) {
                    walkDir(client, listener, dir + file.getName() + "/");
                    continue;
                }
                IndexedFile foundFile =
                        new IndexedFile(file.getName(), "ftp://" + url + dir + file.getName());
                listener.fireNewFileIndexedEvent(foundFile);
            }
        } catch (IOException e) {
            listener.fireErrorEvent(e);
        }
    }
}
