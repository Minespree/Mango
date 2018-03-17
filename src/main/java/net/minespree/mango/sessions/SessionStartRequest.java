package net.minespree.mango.sessions;

import java.net.InetAddress;
import java.util.UUID;

/**
 * @since 10/02/2018
 */
public class SessionStartRequest {
    private String serverId;
    private UUID playerUuid;
    private InetAddress ip;

    // TODO Add Mongo load method
}
