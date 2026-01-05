package org.carpenoctemcloud.server;

import java.util.List;
import org.carpenoctemcloud.CNCloudTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CNCloudTest
class ServerIndexerFactoryTest {

    @Autowired
    private ServerService serverService;

    @Test
    public void allServerHaveIndexerTest() {
        // Checks if all servers have an indexer.
        List<Server> servers = serverService.getServers();

        for (Server server : servers) {
            assertDoesNotThrow(() -> ServerIndexerFactory.getIndexer(server));
        }

        Server fakeServer = new Server(0, "test.student.utwente.nl", "fake-protocol", "file");
        assertThrows(ServerProtocolNotFoundException.class,
                     () -> ServerIndexerFactory.getIndexer(fakeServer));
    }
}