package org.carpenoctemcloud.download_history;

import org.carpenoctemcloud.CNCloudTest;
import org.carpenoctemcloud.account.AccountService;
import org.carpenoctemcloud.configuration.ConfigurationConstants;
import org.carpenoctemcloud.indexing.IndexedFile;
import org.carpenoctemcloud.redirect_files.VlcRedirectFileCreator;
import org.carpenoctemcloud.remote_file.RemoteFile;
import org.carpenoctemcloud.remote_file.RemoteFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CNCloudTest
class DownloadHistoryServiceTest {

    private final int FILES_TO_ADD = ConfigurationConstants.MAX_FETCH_SIZE_HISTORY + 1;
    private final IndexedFile fakeFile = new IndexedFile("/fake/", "fake.txt", "univac.student.utwente.nl");
    @Autowired
    private DownloadHistoryService downloadHistoryService;

    private int accountID;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RemoteFileService remoteFileService;

    @BeforeEach
    public void setup() {
        accountID = accountService.createAccount("test", "test@student.utwente.nl", "12345678", false);
        accountService.activateAccount(accountID);
        remoteFileService.addRemoteFile(fakeFile);

        for (int i = 0; i < FILES_TO_ADD; i++) {
            downloadHistoryService.addFileToHistory(accountID, 1, new VlcRedirectFileCreator().RedirectFileCreatorName());
        }
    }

    @Test
    public void filesInHistoryTest() {
        List<RemoteFile> ret = downloadHistoryService.getRecentHistory(accountID);
        assertEquals(ConfigurationConstants.MAX_FETCH_SIZE_HISTORY, ret.size());
        for (RemoteFile r : ret) {
            assertEquals(fakeFile.filename(), r.name());
        }
    }

    @Test
    public void invalidAccountsTest() {
        List<RemoteFile> ret = downloadHistoryService.getRecentHistory(13);
        assertTrue(ret.isEmpty());
        downloadHistoryService.addFileToHistory(null, 1, new VlcRedirectFileCreator().RedirectFileCreatorName());
        ret = downloadHistoryService.getRecentHistory(13);
        assertTrue(ret.isEmpty());
    }
}