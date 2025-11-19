package org.carpenoctemcloud.ftp;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
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
            int replyCode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                client.disconnect();
                listener.fireErrorEvent(
                        new Exception("Error code for ftp completion: " + replyCode));
                listener.fireIndexingCompleteEvent();
                return;
            }

            client.login("h.j.smits@student.utwente.nl", "PASS");
        } catch (IOException e) {
            listener.fireErrorEvent(e);
            listener.fireIndexingCompleteEvent();
            return;
        }


        try {
            for (FTPFile file : client.listFiles()) {
                IndexedFile foundFile = new IndexedFile(file.getName(), file.getLink());
                listener.fireNewFileIndexedEvent(foundFile);
            }
        } catch (IOException e) {
            listener.fireErrorEvent(e);
        }
        listener.fireIndexingCompleteEvent();
    }
}
