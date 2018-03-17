package net.minespree.mango.mongo;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.mongodb.client.model.Filters;
import net.minespree.mango.util.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @since 09/02/2018
 */
public class BsonUtil {
    public static Stream<String> stringListToStream(Document document, String key) {
        Preconditions.checkNotNull(document);
        Preconditions.checkNotNull(key);

        List<Object> list = (List<Object>) document.get(key);

        if (list == null) {
            return null;
        }

        return list.stream().filter(Objects::nonNull).map(String::valueOf);
    }

    public static Set<String> stringListToSet(Document document, String key) {
        Stream<String> stream = stringListToStream(document, key);

        if (stream == null) {
            return Sets.newHashSet();
        }

        return stream.collect(Collectors.toSet());
    }

    public static Set<UUID> uuidListToSet(Document document, String key) {
        Stream<String> stream = stringListToStream(document, key);

        if (stream == null) {
            return Sets.newHashSet();
        }

        return stream.map(UUID::fromString).collect(Collectors.toSet());
    }

    /**
     * Returns a casted {@link Document} contained on the {@code parent} document,
     * or a new empty {@link Document} if not present.
     */
    public static Document getSubDocument(Document parent, String key) {
        return (Document) parent.getOrDefault(key, new Document());
    }

    /**
     * Gets the String value stored on {@code document.key} via {@link Document#getString(Object)}.
     * Will return {@code null} if the value is null or if the {@link String} is empty.
     * @see String#isEmpty()
     */
    public static String getNonEmptyString(Document document, String key) {
        String value = document.getString(key);

        return StringUtils.nonEmpty(value).orElse(null);
    }

    public static Bson getUUIDIdFilter(UUID uuid) {
        return Filters.eq("_id", uuid.toString());
    }

    // TODO Move to Date utils

    public static Date toMongoDate(LocalDate date) {
        Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(instant);
    }

    public static Date toMongoDate(LocalDateTime time) {
        Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(instant);
    }
}
