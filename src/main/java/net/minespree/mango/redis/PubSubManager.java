package net.minespree.mango.redis;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minespree.mango.collections.MapUtils;
import net.minespree.mango.serialization.JsonUtils;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.Map;

/**
 * @since 09/02/2018
 */
public class PubSubManager extends JedisPubSub {
    private final Map<String, PubSubListener> listeners = Maps.newConcurrentMap();

    @Override
    public void onMessage(String channel, String message) {
        try {
            JsonObject object = JsonUtils.parseObject(message);

            MapUtils.ifPresent(listeners, channel, listener -> {
                listener.receive(channel, object);
            });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(PubSubListener listener, String... channels) {
        MapUtils.putAll(listeners, channels, listener);

        // Send SUBSCRIBE message
        super.subscribe(channels);
    }

    @Override
    public void unsubscribe(String... channels) {
        Arrays.stream(channels).forEach(listeners::remove);

        super.unsubscribe(channels);
    }

    @Override
    public void unsubscribe() {
        listeners.clear();
        super.unsubscribe();
    }

    @Override
    public void subscribe(String... channels) {
        throw new UnsupportedOperationException("A PubSubListener instance is required");
    }
}
