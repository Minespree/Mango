package net.minespree.mango.mongo;

import org.mongodb.morphia.mapping.DefaultCreator;
import org.mongodb.morphia.mapping.MappingException;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;

/**
 * Object factory implementation that creates "dummy" constuctors
 * needed for serialization of {@code final} fields on the mapping phase.
 * @since 10/02/2018
 * @see <a href="https://blog.jayway.com/2012/02/28/configure-morphia-to-work-without-a-default-constructor/">Configure Morphia to work without a default constructor</a>
 */
public class MorphiaModelFactory extends DefaultCreator {
    @Override
    public <T> T createInstance(Class<T> clazz) {
        try {
            final Constructor<T> constructor = getNoArgsConstructor(clazz);

            if (constructor != null) {
                return constructor.newInstance();
            }

            try {
                final Constructor<?> serializeConstructor = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(clazz, Object.class.getDeclaredConstructor(null));

                return (T) serializeConstructor.newInstance(null);
            } catch (Exception e) {
                throw new MappingException("Failed to instantiate " + clazz.getName(), e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Constructor<T> getNoArgsConstructor(final Class<T> type) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);

            return constructor;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
