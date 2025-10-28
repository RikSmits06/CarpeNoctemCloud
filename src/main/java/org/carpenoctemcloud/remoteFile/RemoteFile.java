package org.carpenoctemcloud.remoteFile;

import java.sql.Timestamp;


public record RemoteFile(Long id, String name, String downloadURL, Timestamp lastIndexed) {

}
