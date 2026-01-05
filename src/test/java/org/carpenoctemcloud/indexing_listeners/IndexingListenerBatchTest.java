package org.carpenoctemcloud.indexing_listeners;

import org.carpenoctemcloud.CNCloudTest;
import org.springframework.beans.factory.annotation.Autowired;

@CNCloudTest
public class IndexingListenerBatchTest extends IndexingListenerTest<IndexingListenerBatch> {

    @Autowired
    private IndexingListenerBatch listener;

    public IndexingListenerBatchTest() {
    }

    @Override
    protected IndexingListenerBatch getInstance() {
        return listener;
    }
}
