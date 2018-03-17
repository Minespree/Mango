package net.minespree.mango.time;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.Set;

/**
 * @since 09/02/2018
 */
public final class FriendlyUnits {
    private static final Set<FriendlyUnits> DESC = ImmutableSet.of(
        new FriendlyUnits(ChronoUnit.YEARS, "year"),
        new FriendlyUnits(ChronoUnit.MONTHS, "month"),
        new FriendlyUnits(ChronoUnit.WEEKS, "week"),
        new FriendlyUnits(ChronoUnit.DAYS, "day"),
        new FriendlyUnits(ChronoUnit.HOURS, "hour"),
        new FriendlyUnits(ChronoUnit.MINUTES, "minute"),
        new FriendlyUnits(ChronoUnit.SECONDS, "second"),
        new FriendlyUnits(ChronoUnit.MILLIS, "millisecond")
    );

    private static final FriendlyUnits LARGEST = DESC.iterator().next();
    private static final FriendlyUnits SMALLEST = Iterables.getLast(DESC);

    public static Set<FriendlyUnits> descending() {
        return DESC;
    }

    public static FriendlyUnits largest() {
        return LARGEST;
    }

    public static FriendlyUnits smallest() {
        return SMALLEST;
    }

    private static final Map<TemporalUnit, FriendlyUnits> MAP = Maps.uniqueIndex(DESC, unit -> unit.unit);

    public static FriendlyUnits get(TemporalUnit type) {
        return MAP.get(type);
    }

    public final TemporalUnit unit;
    public final String singularKey, pluralKey;

    private FriendlyUnits(TemporalUnit unit, String singularKey) {
        this.unit = unit;
        this.singularKey = "time.interval." + singularKey;
        this.pluralKey = this.singularKey + "s";
    }

    public String key(boolean plural) {
        return plural ? pluralKey : singularKey;
    }

    public String key(long quantity) {
        return key(quantity != 1);
    }
}
