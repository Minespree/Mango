package net.minespree.mango.util;

import com.google.common.collect.*;
import net.minespree.mango.exception.AmbiguousElementException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Collections.emptySet;
import static java.util.function.Function.identity;

/**
 * @since 09/02/2018
 */
public final class Collectors {
    private Collectors() {}

    public static <T> Collector<T, ?, Optional<T>> zeroOrOne() {
        return java.util.stream.Collectors.reducing((a, b) -> {
            throw new AmbiguousElementException();
        });
    }

    public static <T> Collector<T, ?, ImmutableList<T>> toImmutableList() {
        return new ListCollector<>(ImmutableList::copyOf);
    }

    public static <T> Collector<T, ?, ImmutableList<T>> toReverseImmutableList() {
        return new ListCollector<>(f -> ImmutableList.copyOf(Lists.reverse(f)));
    }

    public static <T> Collector<T, ?, ArrayList<T>> toArrayList() {
        return new ListCollector<>(identity());
    }

    public static <T> Collector<T, ?, ImmutableSet<T>> toImmutableSet() {
        return new ListCollector<>(ImmutableSet::copyOf);
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, ImmutableMap<K, V>> toImmutableMap() {
        return toImmutableMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <T, K> Collector<T, ?, ImmutableMap<K, T>> indexingBy(Function<? super T, ? extends K> keyMapper) {
        return toImmutableMap(keyMapper, identity());
    }

    public static <T, V> Collector<T, ?, ImmutableMap<T, V>> mappingTo(Function<? super T, ? extends V> valueMapper) {
        return toImmutableMap(identity(), valueMapper);
    }

    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyMapper,
                                                                               Function<? super T, ? extends V> valueMapper) {
        return new Collector<T, ImmutableMap.Builder<K, V>, ImmutableMap<K, V>>() {
            @Override
            public Supplier<ImmutableMap.Builder<K, V>> supplier() {
                return ImmutableMap::builder;
            }

            @Override
            public BiConsumer<ImmutableMap.Builder<K, V>, T> accumulator() {
                return (builder, t) -> {
                    final V value = valueMapper.apply(t);
                    if(value != null) {
                        builder.put(keyMapper.apply(t), value);
                    }
                };
            }

            @Override
            public BinaryOperator<ImmutableMap.Builder<K, V>> combiner() {
                return (b1, b2) -> {
                    b1.putAll(b2.build());
                    return b1;
                };
            }

            @Override
            public Function<ImmutableMap.Builder<K, V>, ImmutableMap<K, V>> finisher() {
                return ImmutableMap.Builder::build;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return emptySet();
            }
        };
    }

    private static class ListCollector<T, R> implements Collector<T, ArrayList<T>, R> {

        private final Function<ArrayList<T>, R> finisher;

        protected ListCollector(Function<ArrayList<T>, R> finisher) {
            this.finisher = finisher;
        }

        @Override
        public Supplier<ArrayList<T>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<ArrayList<T>, T> accumulator() {
            return List::add;
        }

        @Override
        public BinaryOperator<ArrayList<T>> combiner() {
            return (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            };
        }

        @Override
        public Function<ArrayList<T>, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return emptySet();
        }
    }
}
