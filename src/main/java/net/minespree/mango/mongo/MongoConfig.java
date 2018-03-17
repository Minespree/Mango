package net.minespree.mango.mongo;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Value;
import net.minespree.mango.validator.Validators;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @since 09/02/2018
 */
@Value
public class MongoConfig {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public MongoConfig(String host, int port, String database, String username, String password) {
        this.host = checkNotNull(host);
        this.port = Validators.validatePort(port);
        this.database = checkNotNull(database);
        this.username = checkNotNull(username);
        this.password = password;
    }

    public List<ServerAddress> getSeeds() {
        return Collections.singletonList(
            new ServerAddress(host, port)
        );
    }

    public List<MongoCredential> getCredentials() {
        return Collections.singletonList(
            MongoCredential.createCredential(username, database, password.toCharArray())
        );
    }
}
