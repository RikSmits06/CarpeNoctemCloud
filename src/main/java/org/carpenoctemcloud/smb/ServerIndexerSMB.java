package org.carpenoctemcloud.smb;

import java.net.MalformedURLException;
import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.Configuration;
import jcifs.config.BaseConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.carpenoctemcloud.indexing.IndexedFile;
import org.carpenoctemcloud.indexing.IndexingListener;
import org.carpenoctemcloud.indexing.ServerIndexer;


/**
 * Indexer for a SMB server. Will index all shares it can access.
 */
public class ServerIndexerSMB implements ServerIndexer {

    private final String serverURL;

    /**
     * Creates a new indexer of the given server url.
     *
     * @param serverURL The url of the server, such as spitfire.student.utwente.nl.
     */
    public ServerIndexerSMB(String serverURL) {
        this.serverURL = serverURL;
    }

    @Override
    public void indexServer(IndexingListener listener) {
        Configuration config;

        try {
            config = new BaseConfiguration(true);
        } catch (CIFSException e) {
            listener.fireErrorEvent(e);
            listener.fireIndexingCompleteEvent();
            return;
        }

        NtlmPasswordAuthenticator auth = new NtlmPasswordAuthenticator("", "CarpeNoctemCloud", "");
        CIFSContext context = new BaseContext(config);
        context.withCredentials(auth);

        SmbFile[] shares;
        try (SmbFile host = new SmbFile("smb://" + this.serverURL, context)) {
            shares = host.listFiles();
        } catch (MalformedURLException | SmbException e) {
            listener.fireErrorEvent(e);
            listener.fireIndexingCompleteEvent();
            return;
        }

        for (SmbFile share : shares) {
            try (SmbFile file = new SmbFile(share.toString(), context)) {
                walkDirectory(file, listener);
            } catch (MalformedURLException e) {
                listener.fireErrorEvent(e);
            }
        }

        listener.fireIndexingCompleteEvent();
    }

    private void walkDirectory(SmbFile dir, IndexingListener listener) {
        try {
            if (dir.isFile()) {
                listener.fireErrorEvent(
                        new IllegalArgumentException("Given dir was not an actual directory."));
                return;
            }
            listener.fireNewDirectoryEvent(serverURL, dir.getPath()
                    .substring(("smb://" + serverURL).length()));
            for (SmbFile entry : dir.listFiles()) {
                if (entry.isDirectory()) {
                    walkDirectory(entry, listener);
                    continue;
                }

                String path = entry.getPath();
                String fileName = entry.getName();
                path = path.substring(("smb://" + serverURL).length(),
                                      path.length() - fileName.length());
                listener.fireNewFileIndexedEvent(new IndexedFile(path, fileName, serverURL));
            }
        } catch (SmbException e) {
            listener.fireErrorEvent(e);
        }
    }
}
