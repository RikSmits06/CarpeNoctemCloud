package org.carpenoctemcloud.indexing_listeners;

import org.carpenoctemcloud.indexing.IndexedFile;
import org.carpenoctemcloud.indexing.IndexingListener;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


public abstract class IndexingListenerTest<T extends IndexingListener> {

    private final Random random = new Random();
    @Autowired
    private RemoteFileService remoteFileService;

    protected abstract T getInstance();

    @Test
    public void countAndRetrieveTest() {
        IndexingListener listener = getInstance();
        assertEquals(0, listener.getTotalFilesIndexed());
        assertEquals(0, remoteFileService.getNumberOfRemoteFiles());
        final int FILES_TO_CREATE = random.nextInt(50, 500);
        final HashSet<String> alreadyUsed = new HashSet<>();
        for (int i = 0; i < FILES_TO_CREATE; i++) {
            String filename = "File" + i + ".mkv";
            alreadyUsed.add(filename);
            String path = "/" + i + "/";
            String host = "spitfire.student.utwente.nl";
            IndexedFile indexedFile = new IndexedFile(path, filename, host);
            listener.fireNewDirectoryEvent(host, path);
            listener.fireNewFileIndexedEvent(indexedFile);
        }
        listener.fireIndexingCompleteEvent();

        assertEquals(FILES_TO_CREATE, listener.getTotalFilesIndexed());
        assertEquals(FILES_TO_CREATE, remoteFileService.getNumberOfRemoteFiles());
    }
}
