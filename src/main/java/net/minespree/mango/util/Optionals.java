package net.minespree.mango.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @since 10/02/2018
 */
public final class Optionals {
    private Optionals() {}

    public static boolean equals(Optional<?> a, Optional<?> b) {
        return a.isPresent() && b.isPresent() && a.get().equals(b.get());
    }

    public static boolean equals(Optional<?> a, Object b) {
        return a.isPresent() && a.get().equals(b);
    }

    public static boolean equals(Object a, Optional<?> b) {
        return equals(b, a);
    }

    public static boolean isInstance(Optional<?> value, Class<?> type) {
        return value.isPresent() && type.isInstance(value.get());
    }

    public static <T> Optional<T> cast(Optional<? super T> value, Class<T> type) {
        return isInstance(value, type) ? (Optional<T>) value
                                       : Optional.empty();
    }

    public static <T> Optional<T> cast(Object obj, Class<T> type) {
        return type.isInstance(obj) ? Optional.ofNullable((T) obj)
                                    : Optional.empty();
    }

    public static <T> Optional<T> getIf(boolean condition, Supplier<T> supplier) {
        return condition ? Optional.of(supplier.get())
                         : Optional.empty();
    }

    public static <T> Optional<T> filter(T value, Predicate<? super T> filter) {
        return Optional.of(value).filter(filter);
    }

    public static <T> Set<T> toSet(Optional<T> optional) {
        return optional.isPresent() ? Collections.singleton(optional.get())
                                    : Collections.emptySet();
    }

    public static <T> Stream<T> stream(Optional<T> optional) {
        return optional.map(Stream::of).orElse(Stream.empty());
    }

    public static <T> Set<T> union(Stream<Optional<? extends T>> optionals) {
        return optionals.filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public static <T> Set<T> union(Collection<Optional<? extends T>> optionals) {
        return union(optionals.stream());
    }

    public static <T> Set<T> union(Optional<? extends T>... optionals) {
        return union(Stream.of(optionals));
    }

    public static <T> Optional<T> first(Iterable<T> iterable) {
        final Iterator<T> iterator = iterable.iterator();

        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
    }

    public static <T> Optional<T> first(Stream<Optional<? extends T>> options) {
        return (Optional<T>) options.filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }

    public static <T> Optional<T> first(Collection<Optional<? extends T>> options) {
        return first(options.stream());
    }

    public static <T> Optional<T> first(Optional<? extends T>... options) {
        return first(Stream.of(options));
    }
}
