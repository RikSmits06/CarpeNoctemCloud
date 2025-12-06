package org.carpenoctemcloud.server;

import java.util.List;
import java.util.Optional;
import org.carpenoctemcloud.CNCloudTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@CNCloudTest
class ServerServiceTest {

    @Autowired
    private ServerService serverService;

    @Test
    public void allServersTest() {
        List<Server> servers = serverService.getServers();
        assertFalse(servers.isEmpty());

        for (Server server : servers) {
            Optional<Server> serverByHost = serverService.getServerByHost(server.host());
            Optional<Server> serverByID = serverService.getServerByID(server.id());
            assertTrue(serverByHost.isPresent());
            assertTrue(serverByID.isPresent());
            assertEquals(server, serverByHost.get());
            assertEquals(server, serverByID.get());
        }
    }
}