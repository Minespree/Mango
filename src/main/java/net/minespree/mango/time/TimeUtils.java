package net.minespree.mango.time;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * @since 09/02/2018
 */
public class TimeUtils {
    public static final Duration INF_POSITIVE = ChronoUnit.FOREVER.getDuration();
    public static final Duration INF_NEGATIVE = INF_POSITIVE.negated();

    public static boolean isFinite(Duration duration) {
        return !isInfPositive(duration) && !isInfNegative(duration);
    }

    public static boolean isInfPositive(Duration duration) {
        return INF_POSITIVE.equals(duration);
    }

    public static boolean isInfNegative(Duration duration) {
        return INF_NEGATIVE.equals(duration);
    }

    public static long toUnit(TemporalUnit unit, Duration duration) {
        switch ((ChronoUnit) unit) {
            case NANOS:
                return duration.toNanos();
            case MICROS:
                return toMicros(duration);
            case MILLIS:
                return duration.toMillis();
            case SECONDS:
                return duration.getSeconds();
        }

        if (unit.getDuration().getNano() == 0) {
            return duration.getSeconds() / unit.getDuration().getSeconds();
        }

        throw new IllegalArgumentException("Unsupported sub-second unit " + unit);
    }

    public static long toMicros(Duration duration) {
        return Math.addExact(Math.multiplyExact(duration.getSeconds(), 1_000_000),
                duration.getNano() / 1_000);
    }

    public static long toTicks(Duration duration) {
        return duration.toMillis() / 50;
    }

    public static Duration fromTicks(long ticks) {
        return Duration.ofMillis(50 * ticks);
    }

    public static Duration min(Duration a, Duration b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    public static Duration max(Duration a, Duration b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static Instant min(Instant a, Instant b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    public static Instant max(Instant a, Instant b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static boolean isEqualOrBeforeNow(Instant now, Instant instant) {
        return !instant.isAfter(now);
    }

    public static boolean isEqualOrBeforeNow(Instant instant) {
        return isEqualOrBeforeNow(Instant.now(), instant);
    }

    public static boolean isEqualOrAfterNow(Instant now, Instant instant) {
        return !instant.isBefore(now);
    }

    public static boolean isEqualOrAfterNow(Instant instant) {
        return isEqualOrAfterNow(Instant.now(), instant);
    }
}
