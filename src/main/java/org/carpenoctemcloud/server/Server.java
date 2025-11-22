package org.carpenoctemcloud.server;

public record Server(int id, String host, String protocol, String downloadPrefix) {
}
