package net.minespree.mango.time;

import lombok.Value;

import java.time.Instant;

/**
 * @since 09/02/2018
 */
@Value
public class Interval {
    private Instant start;
    private Instant end;

    public static Interval between(Instant start, Instant end) {
        return new Interval(start, end);
    }
}
