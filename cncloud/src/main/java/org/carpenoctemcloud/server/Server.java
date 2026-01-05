package org.carpenoctemcloud.server;

/**
 * Entity representing the server table.
 *
 * @param id             The id of the server.
 * @param host           The hostname of the server.
 * @param protocol       The protocol used by the server.
 * @param downloadPrefix The prefix which should be used when constructing a download url.
 */
public record Server(int id, String host, String protocol, String downloadPrefix) {
}
