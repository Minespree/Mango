package net.minespree.mango.status;

import lombok.RequiredArgsConstructor;
import net.minespree.mango.redis.RedisManager;
import net.minespree.mango.scheduler.Scheduler;
import net.minespree.mango.serialization.JsonUtils;
import net.minespree.mango.server.Instance;
import redis.clients.jedis.Jedis;

/**
 * @since 15/02/2018
 */
@RequiredArgsConstructor
public class BaseStatusUpdater implements StatusUpdater {
    public static final String REDIS_PREFIX = "instance-statuses:";

    private final Scheduler scheduler;
    private final RedisManager manager;

    @Override
    public void update(Instance instance) {
        String key = getServerKey(instance);

        scheduler.submit(() -> {
            try (Jedis jedis = manager.getResource()) {
                jedis.hset(
                    key,
                    instance.getServerId(),
                    JsonUtils.toJson(instance.getCurrentStatus())
                );
            }
        });

    }

    private String getServerKey(Instance instance) {
        return REDIS_PREFIX + instance.getServerGroup();
    }

    @Override
    public void onShutdown(Instance instance) {
        String key = getServerKey(instance);

        try (Jedis jedis = manager.getResource()) {
            jedis.hdel(key, instance.getServerId());
        }
    }
}
