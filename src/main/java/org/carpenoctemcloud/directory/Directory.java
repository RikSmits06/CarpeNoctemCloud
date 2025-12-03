package org.carpenoctemcloud.directory;

import java.util.Optional;

public record Directory(long id, String path, String folderName, Optional<Long> parentDirectory,
                        int serverID) {
}
