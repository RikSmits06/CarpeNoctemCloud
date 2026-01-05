package org.carpenoctemcloud.indexing_listeners;


import org.carpenoctemcloud.CNCloudTest;
import org.springframework.beans.factory.annotation.Autowired;

@CNCloudTest
class IndexingListenerImplTest extends IndexingListenerTest<IndexingListenerImpl> {

    @Autowired
    private IndexingListenerImpl listener;

    public IndexingListenerImplTest() {
    }

    @Override
    protected IndexingListenerImpl getInstance() {
        return listener;
    }
}