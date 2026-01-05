package org.carpenoctemcloud.indexing;

/**
 * Representation of a file which was indexed.
 *
 * @param path     The path to the file except the filename.
 * @param filename The name of the file which was found.
 *                 If the file was in server/films/movie.mkv the name will just be movie.mkv.
 * @param host     The domain name of the host/server.
 */
public record IndexedFile(String path, String filename, String host) {
}
