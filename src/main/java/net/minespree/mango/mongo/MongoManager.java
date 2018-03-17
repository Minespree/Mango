package net.minespree.mango.mongo;

import com.mongodb.MongoClient;
import lombok.Getter;
import net.minespree.mango.connectable.SimpleConnectable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.converters.TypeConverter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @since 09/02/2018
 */
public class MongoManager extends SimpleConnectable {
    private final MongoConfig config;

    @Getter private Morphia morphia;
    @Getter private MongoClient client;
    @Getter private Datastore datastore;

    public MongoManager(MongoConfig config) {
        this.config = checkNotNull(config);

        // Mongo logs too much stuff by default
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
    }

    @Override
    public void connect() throws IOException {
        super.connect();

        client = new MongoClient(config.getSeeds(), config.getCredentials());
        morphia = new Morphia();

        // Creates constructors needed for serialization on the fly
        morphia.getMapper().getOptions().setObjectFactory(new MorphiaModelFactory());

        datastore = morphia.createDatastore(client, config.getDatabase());
        datastore.ensureIndexes();
    }

    @Override
    public void disconnect() throws IOException {
        super.disconnect();

        client.close();
        client = null;
        morphia = null;
        datastore = null;
    }

    public void register(Class... entityClasses) {
        morphia.map(entityClasses);
    }

    public void register(String packageName) {
        morphia.mapPackage(packageName);
    }

    public boolean isRegistered(Class clazz) {
        return morphia.isMapped(clazz);
    }

    public void addConverter(TypeConverter typeConverter) {
        morphia.getMapper().getConverters().addConverter(typeConverter);
    }
}
