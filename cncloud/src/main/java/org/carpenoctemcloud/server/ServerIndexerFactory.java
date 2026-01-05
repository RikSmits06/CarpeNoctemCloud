package org.carpenoctemcloud.server;

import org.carpenoctemcloud.indexing.ServerIndexer;
import org.carpenoctemcloud.smb.ServerIndexerSMB;

/**
 * Factory class to get the right indexer for a given server.
 */
public class ServerIndexerFactory {

    private ServerIndexerFactory() {
    }

    /**
     * Gets an indexer based on which server needs to be indexed.
     *
     * @param server The server which has to be indexed.
     * @return The right indexer for the server.
     * @throws ServerProtocolNotFoundException When the server protocol does not have an indexer.
     */
    @SuppressWarnings("")
    public static ServerIndexer getIndexer(Server server) throws ServerProtocolNotFoundException {
        switch (server.protocol()) {
            case "smb" -> {
                return new ServerIndexerSMB(server.host());
            }
            default -> throw new ServerProtocolNotFoundException(
                    "Server with protocol " + server.protocol() + " does not have an indexer.");
        }
    }
}
