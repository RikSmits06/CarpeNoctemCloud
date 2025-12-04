package org.carpenoctemcloud.server;

import org.carpenoctemcloud.indexing.ServerIndexer;
import org.carpenoctemcloud.smb.ServerIndexerSMB;

public class ServerIndexerFactory {

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
