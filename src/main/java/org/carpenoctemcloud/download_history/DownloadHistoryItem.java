package org.carpenoctemcloud.download_history;

import java.sql.Timestamp;

public record DownloadHistoryItem(Integer accountID, int fileID, Timestamp time) {
}
