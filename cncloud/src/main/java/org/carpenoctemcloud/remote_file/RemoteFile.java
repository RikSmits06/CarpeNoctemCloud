package org.carpenoctemcloud.remote_file;

import java.sql.Timestamp;
import java.util.Optional;


/**
 * The RemoteFile is an entry in the database which contains an indexed reference to a file on a server.
 *
 * @param id           The primary key of the RemoteFile.
 * @param name         The name of the indexed file without its full path.
 * @param directory_id The id of the directory of the remote file.
 * @param lastIndexed  The time at which it was indexed last.
 * @param categoryID   The ID of the category which the item is in.
 */
public record RemoteFile(long id, String name, long directory_id, Timestamp lastIndexed,
                         Optional<Integer> categoryID) {

}
