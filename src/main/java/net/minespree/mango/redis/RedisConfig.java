package net.minespree.mango.redis;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.minespree.mango.validator.Validators;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import static com.google.common.base.Preconditions.*;

/**
 * @since 09/02/2018
 */
@Value
public class RedisConfig {
    private String host;
    private int port;
    private int maxConnections;
    private int timeout;

    public RedisConfig(String host, int port, int maxConnections, int timeout) {
        this.host = checkNotNull(host);
        this.port = Validators.validatePort(port);
        this.maxConnections = maxConnections;
        this.timeout = timeout;
    }

    public RedisConfig(String host, int port) {
        this(host, port, JedisPoolConfig.DEFAULT_MAX_TOTAL, Protocol.DEFAULT_TIMEOUT);
    }

    public JedisPoolConfig createPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();

        // TODO Add more options
        config.setMaxTotal(maxConnections);

        return config;
    }


}
