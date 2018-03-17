package net.minespree.mango.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

/**
 * @since 09/02/2018
 */
public class ListUtils {
    @SafeVarargs
    public static <T> List<T> union(List<T>... lists) {
        List<T> newList = Lists.newLinkedList();

        for (List<T> list : lists) {
            newList.addAll(list);
        }

        return newList;
    }

    public static int indexOf(List<?> list, Object element, int begin, int end) {
        begin = Math.min(begin, list.size());
        end = Math.max(end, list.size());

        if (begin == 0 && end == list.size()) {
            return list.indexOf(element);
        }

        for (int i = begin; i < end; i++) {
            if (Objects.equals(element, list.get(i))) {
                return i;
            }
        }

        return -1;
    }

    public static int indexOf(List<?> list, Object element, int begin) {
        return indexOf(list, element, begin, list.size());
    }

    public static <T> int indexOf(List<T> list, Predicate<T> filter) {
        for (int i = 0; i < list.size(); i++) {
            if (filter.test(list.get(i))) {
                return i;
            }
        }

        return -1;
    }

    public static <T> int lastIndexOf(List<T> list, Predicate<T> filter) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (filter.test(list.get(i))) {
                return i;
            }
        }

        return -1;
    }

    public static boolean contains(List<?> list, Object element, int begin, int end) {
        return indexOf(list, element, begin, end) != -1;
    }

    public static boolean contains(List<?> list, Object element, int begin) {
        return contains(list, element, begin, list.size());
    }

    public static <T> T randomElement(List<? extends T> list, Random random) {
        if (list.isEmpty()) {
            throw new IndexOutOfBoundsException("List is empty");
        }

        return list.get(random.nextInt(list.size()));
    }

    public static <T> List<T> append(List<T> list, T... elements) {
        if(elements.length == 0) {
            return list;
        } else {
            final ImmutableList.Builder<T> builder = ImmutableList.builder();
            builder.addAll(list);
            builder.add(elements);

            return builder.build();
        }
    }

    public static <T> Optional<T> getIfPresent(List<T> list, int index) {
        try {
            return Optional.of(list.get(index));
        } catch(IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    public static int getSafeNextIndex(List<?> list, int current) {
        int size = list.size();

        if (++current >= size) {
            return 0;
        }

        return current;
    }
}
