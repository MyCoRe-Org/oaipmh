package org.mycore.oai.pmh;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contains util methods to support the ISO8601 UTC date format.
 * 
 * @author Matthias Eichner
 */
public abstract class DateUtils {

    public static ThreadLocal<Granularity> currentGranularity;

    public static ZoneId UTC_ZONE;

    public static DateTimeFormatter UTC_DAY;

    public static DateTimeFormatter UTC_SECOND;

    static {
        currentGranularity = new ThreadLocal<Granularity>() {
            protected Granularity initialValue() {
                return Granularity.YYYY_MM_DD;
            };
        };
        UTC_ZONE = ZoneId.of("UTC").normalized();
        UTC_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(UTC_ZONE);
        UTC_SECOND = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(UTC_ZONE);
    }

    /**
     * Sets a thread local date granularity.
     * 
     * @param granularity
     *            the granularity to set
     */
    public static void setGranularity(Granularity granularity) {
        currentGranularity.set(granularity);
    }

    /**
     * Returns the thread local date granularity.
     * 
     * @return the date granularity
     */
    public static Granularity getGranularity() {
        return currentGranularity.get();
    }

    /**
     * Formats to YYYY-MM-DDThh:mm:ssZ.
     * 
     * @param instant instant to format
     * @return the formatted time string
     */
    public static String formatUTCSecond(Instant instant) {
        if (instant != null) {
            return UTC_SECOND.format(instant);
        }
        return null;
    }

    /**
     * Formats to YYYY-MM-DD.
     * 
     * @param instant instant to format
     * @return the formatted time string
     */
    public static String formatUTCDay(Instant instant) {
        if (instant != null) {
            return UTC_DAY.format(instant);
        }
        return null;
    }

    /**
     * Formats a Date into a date/time string. The format depends on the {@link Granularity}
     * declared by {@link #setGranularity(Granularity)}.
     * 
     * @param instant
     *            the instant to be formatted into a time string.
     * @return the formatted time string. In YYYY-MM-DDThh:mm:ssZ or YYYY-MM-DD
     */
    public static String format(Instant instant) {
        if (instant == null) {
            return null;
        }
        Granularity granularity = getGranularity();
        if (Granularity.AUTO.equals(granularity)) {
            granularity = guessGranularity(instant);
        }
        return format(instant, granularity);
    }

    /**
     * Formats a date into the given {@link Granularity} date string.
     * 
     * @param instant
     *            instant to be formatted
     * @param granularity
     *            granularity of the date
     * @return the formatted time string. In YYYY-MM-DDThh:mm:ssZ or YYYY-MM-DD
     */
    public static String format(Instant instant, Granularity granularity) {
        if (instant == null) {
            return null;
        }
        if (Granularity.YYYY_MM_DD.equals(granularity)) {
            return UTC_DAY.format(instant);
        } else {
            return UTC_SECOND.format(instant);
        }
    }

    /**
     * Parses text from the beginning of the given string to produce an instant.
     * 
     * @param date
     *            a string to parse
     * @return A Instant parsed from the string.
     */
    public static Instant parse(String date) {
        if (date == null) {
            return null;
        }
        if (date.contains("T") && date.endsWith("Z")) {
            return LocalDateTime.from(UTC_SECOND.parse(date)).toInstant(ZoneOffset.UTC);
        }
        return LocalDate.parse(date).atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    /**
     * Returns the most likely granularity of an instant. If hour, minute,
     * second and nanoseconds of the instant are zero, the day granularity
     * is returned.
     * 
     * @param instant
     *            instant to check
     * @return the most likely granularity of the date
     */
    public static Granularity guessGranularity(Instant instant) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, UTC_ZONE);
        boolean isStartOfDay = zonedDateTime.getHour() == 0 && zonedDateTime.getMinute() == 0
            && zonedDateTime.getSecond() == 0 && zonedDateTime.getNano() == 0;
        return isStartOfDay ? Granularity.YYYY_MM_DD : Granularity.YYYY_MM_DD_THH_MM_SS_Z;
    }

    /**
     * Returns the granularity of a UTC date.
     * 
     * @param date date string to guess
     * @return the granularity of the given date
     */
    public static Granularity guessGranularity(String date) {
        return guessGranularity(parse(date));
    }

    /**
     * Returns a instant at the end of the day.
     *
     * @param instant Instant to calculate end of day from
     * @return instant at the end of the day
     */
    public static Instant endOfDay(Instant instant) {
        return LocalDate.from(instant).atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
    }

    /**
     * Returns a new Date with the hours, milliseconds, seconds and minutes
     * set to 0.
     *
     * @param date Date used in calculating start of day
     * @return Start of <code>date</code>
     */
    public static Instant startOfDay(Instant instant) {
        return LocalDate.from(instant).atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    /**
     * Adds <code>amount</code> days to the <code>instant</code> and returns
     * the resulting time. A negative amount is allowed.
     *
     * @param instant used in calculating days
     * @param amount Amount of increment.
     * 
     * @return the <var>time</var> + <var>amount</var> days
     */
    public static Instant addDays(Instant instant, int amount) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC).plusDays(amount).toInstant(ZoneOffset.UTC);
    }

    /**
     * Adds <code>amount</code> seconds to <code>time</code> and returns
     * the resulting time.
     *
     * @param instant used in calculating seconds
     * @param amount Amount of increment.
     * 
     * @return the <var>time</var> + <var>amount</var> days
     */
    public static Instant addSeconds(Instant instant, int amount) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC).plusSeconds(amount).toInstant(ZoneOffset.UTC);
    }

    /**
     * Return the former instant.
     * 
     * @param instants array of instant
     * @return the former instant or null if all arguments are null
     */
    public static Instant min(Instant... instants) {
        Instant minInstant = null;
        for (Instant instant : instants) {
            if (minInstant == null) {
                minInstant = instant;
            } else if (instant != null && instant.isBefore(minInstant)) {
                minInstant = instant;
            }
        }
        return minInstant;
    }

    /**
     * Return the later instant.
     * 
     * @param instants array of instant
     * @return the latest instant or null if all arguments are null
     */
    public static Instant max(Instant... instants) {
        Instant maxInstant = null;
        for (Instant instant : instants) {
            if (maxInstant == null) {
                maxInstant = instant;
            } else if (instant != null && instant.isAfter(maxInstant)) {
                maxInstant = instant;
            }
        }
        return maxInstant;
    }

    /**
     * Checks if an instant is between from and until. If from or until are null, then they are
     * defined as infinity.
     * 
     * @param isBetween the instant to check
     * @param from the earlier instant
     * @param until the later instant
     * @return true if the instant is between
     */
    public static boolean between(Instant isBetween, Instant from, Instant until) {
        if (isBetween == null) {
            return false;
        }
        return (from == null || isBetween.isAfter(from)) && (until == null || isBetween.isBefore(until));
    }

}
