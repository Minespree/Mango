package net.minespree.mango.serialization;

import net.minespree.mango.repository.RepoManager;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

/**
 * Morphia type converter.
 * @since 10/02/2018
 */
public abstract class RepoSerializer<T extends RepoManager> extends TypeConverter {
    public RepoSerializer(Class<T> clazz) {
        super(clazz);
    }

    public Object decode(Class<?> clazz, Object o, MappedField mappedField) {
        if (o == null) {
            return null;
        }

        return decode(o, mappedField);
    }

    public abstract Object decode(Object dbObject, MappedField optionalExtraInfo);

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        RepoManager manager = (RepoManager) value;

        return super.encode(manager.getEntries(), optionalExtraInfo);
    }
}
