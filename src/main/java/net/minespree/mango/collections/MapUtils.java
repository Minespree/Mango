package net.minespree.mango.collections;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @since 09/02/2018
 */
public final class MapUtils {
    private MapUtils() {
    }

    public static <K, V> void putAbsent(Map<K, V> dest, Map<K, V> source) {
        for (Map.Entry<K, V> entry : source.entrySet()) {
            if (!dest.containsKey(entry.getKey())) {
                dest.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public static <K, V> void putAll(Map<K, V> dest, Collection<K> source, V value) {
        for (K k : source) {
            dest.put(k, value);
        }
    }

    public static <K, V> void putAll(Map<K, V> dest, K[] source, V value) {
        for (K k : source) {
            dest.put(k, value);
        }
    }

    public static <K, V> ImmutableMap<K, V> merge(Map<K, V> dest, Map<K, V> src) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();

        builder.putAll(src);

        for (Map.Entry<K, V> entry : dest.entrySet()) {
            if (!src.containsKey(entry.getKey())) {
                builder.put(entry);
            }
        }

        return builder.build();
    }

    public static <K, V, R> Stream<R> mapEntries(Map<K, V> map, BiFunction<K, V, R> mapper) {
        Stream.Builder<R> builder = Stream.builder();

        map.forEach((k, v) -> {
            builder.add(mapper.apply(k, v));
        });

        return builder.build();
    }

    public static <K, V, R> Stream<R> mapEntries(Multimap<K, V> map, BiFunction<K, V, R> mapper) {
        Stream.Builder<R> builder = Stream.builder();

        map.entries().forEach(e -> {
            builder.add(mapper.apply(e.getKey(), e.getValue()));
        });

        return builder.build();
    }

    public static <K1, V, K2> Map<K2, V> transformKeys(Map<K1, V> map, Function<K1, K2> keyMapper) {
        final ImmutableMap.Builder<K2, V> builder = ImmutableMap.builder();

        map.forEach((k, v) -> {
            builder.put(keyMapper.apply(k), v);
        });

        return builder.build();
    }

    public static <K1, V, K2> Map<K2, V> transformKeys(Map<K1, V> map, BiFunction<K1, V, K2> keyMapper) {
        final ImmutableMap.Builder<K2, V> builder = ImmutableMap.builder();

        map.forEach((k, v) -> {
            builder.put(keyMapper.apply(k, v), v);
        });

        return builder.build();
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> map, Function<V1, V2> valueMapper) {
        final ImmutableMap.Builder<K, V2> builder = ImmutableMap.builder();

        map.forEach((k, v) -> {
            builder.put(k, valueMapper.apply(v));
        });

        return builder.build();
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> map, BiFunction<K, V1, V2> valueMapper) {
        final ImmutableMap.Builder<K, V2> builder = ImmutableMap.builder();

        map.forEach((k, v) -> {
            builder.put(k, valueMapper.apply(k, v));
        });

        return builder.build();
    }

    public static <K, V> Optional<V> value(Map<K, V> map, K key) {
        return Optional.ofNullable(map.get(key));
    }

    public static <R, C, V> Optional<V> value(Table<R, C, V> table, R rowKey, C columnKey) {
        return Optional.ofNullable(table.get(rowKey, columnKey));
    }

    public static <K, V> Optional<V> ifPresent(Map<K, V> map, K key, Consumer<V> consumer) {
        final Optional<V> value = value(map, key);

        value.ifPresent(consumer);

        return value;
    }

    public static <K, V> void forEach(Multimap<K, V> multimap, BiConsumer<? super K, ? super V> consumer) {
        multimap.asMap().forEach((key, values) -> {
            values.forEach(value -> consumer.accept(key, value));
        });
    }
}
