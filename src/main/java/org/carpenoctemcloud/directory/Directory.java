package org.carpenoctemcloud.directory;

import java.util.Optional;

/**
 * Object representing the directory table.
 *
 * @param id              The id of the directory.
 * @param path            Path to the directory relative to the server host.
 * @param folderName      The name of the folder, not actually stored in database but created in the application.
 * @param parentDirectory The id of the parent. otherwise, {@code Optional.empty()}.
 * @param serverID        ID of the server where the directory is stored.
 */
public record Directory(long id, String path, String folderName, Optional<Long> parentDirectory,
                        int serverID) {
}
