package net.minespree.mango.util;

import lombok.Value;

import java.util.UUID;

/**
 * Immutable implementation of a UUID and player name keypair
 * @since 09/02/2018
 */
@Value
public class UUIDNameKeypair {
    private UUID uuid;
    private String name;
}
