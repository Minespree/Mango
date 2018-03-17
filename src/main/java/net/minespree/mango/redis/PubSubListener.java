package net.minespree.mango.redis;

import com.google.gson.JsonObject;

/**
 * @since 09/02/2018
 */
public interface PubSubListener {
    void receive(String channel, JsonObject object);
}
