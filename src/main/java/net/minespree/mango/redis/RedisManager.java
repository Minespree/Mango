package net.minespree.mango.redis;

import lombok.Getter;
import net.minespree.mango.connectable.SimpleConnectable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @since 09/02/2018
 */
@Getter
public class RedisManager extends SimpleConnectable implements Runnable {
    private final RedisConfig config;

    @Getter
    private PubSubManager pubSub;
    private JedisPool pool;

    public RedisManager(RedisConfig config) {
        this.config = checkNotNull(config);
    }

    @Override
    public void connect() throws IOException {
        super.connect();

        JedisPoolConfig poolConfig = config.createPoolConfig();
        pool = new JedisPool(poolConfig, config.getHost(), config.getPort(), config.getTimeout());

        this.pubSub = new PubSubManager();
    }

    @Override
    public void disconnect() throws IOException {
        super.disconnect();

        pool.close();
        pubSub = null;
        pool = null;
    }

    @Override
    public void run() {
        long sleepMillis = 1000;

        while (true) {
            try (Jedis jedis = getResource()) {
                jedis.subscribe(pubSub);
            } catch (JedisConnectionException e) {
                e.printStackTrace();

                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e1) {
                    return;
                }

                sleepMillis *= 2;
            }
        }
    }

    public Jedis getResource() {
        return pool.getResource();
    }
}
