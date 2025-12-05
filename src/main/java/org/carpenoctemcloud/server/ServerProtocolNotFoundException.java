package org.carpenoctemcloud.server;

/**
 * Exception thrown when a given server protocol does not have an indexer.
 * May also be thrown if a protocol is received which is not known or used.
 */
public class ServerProtocolNotFoundException extends Exception {

    /**
     * Creates a new exception with a message.
     *
     * @param message The message describing the exception.
     */
    public ServerProtocolNotFoundException(String message) {
        super(message);
    }
}
