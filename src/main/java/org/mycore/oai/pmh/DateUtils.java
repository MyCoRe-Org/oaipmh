package org.mycore.oai.pmh;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Contains util methods to support the ISO8601 UTC date format.
 * 
 * @author Matthias Eichner
 */
public abstract class DateUtils {

    private static Logger LOGGER = Logger.getLogger(DateUtils.class);

    private static Calendar CALENDAR = Calendar.getInstance();

    public static ThreadLocal<Granularity> currentGranularity = new ThreadLocal<Granularity>() {
        protected Granularity initialValue() {
            return Granularity.YYYY_MM_DD;
        };
    };

    public static final ThreadLocal<DateFormat> utcDay = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        };
    };

    public static final ThreadLocal<DateFormat> utcSecond = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        };
    };

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
     * @param date date to format
     * @return the formatted time string
     */
    public static String formatUTCSecond(Date date) {
        return utcSecond.get().format(date);
    }

    /**
     * Formats to YYYY-MM-DD.
     * 
     * @param date date to format
     * @return the formatted time string
     */
    public static String formatUTCDay(Date date) {
        return utcDay.get().format(date);
    }

    /**
     * Formats a Date into a date/time string. The format depends on the {@link Granularity}
     * declared by {@link #setGranularity(Granularity)}.
     * 
     * @param date
     *            the time value to be formatted into a time string.
     * @return the formatted time string. In YYYY-MM-DDThh:mm:ssZ or YYYY-MM-DD
     */
    public static String formatUTC(Date date) {
        if (date == null) {
            return null;
        }
        Granularity g = getGranularity();
        if(Granularity.AUTO.equals(g)) {
            g = getGranularity(date);
        }
        if (Granularity.YYYY_MM_DD.equals(g)) {
            return utcDay.get().format(date);
        } else {
            return utcSecond.get().format(date);
        }
    }

    /**
     * Formats a date into the given {@link Granularity} date string.
     * 
     * @param date
     *            date to be formatted
     * @param granularity
     *            granularity of the date
     * @return the formatted time string. In YYYY-MM-DDThh:mm:ssZ or YYYY-MM-DD
     */
    public static String formatUTC(Date date, Granularity granularity) {
        if(date == null) {
            return null;
        }
        if (Granularity.YYYY_MM_DD.equals(granularity)) {
            return utcDay.get().format(date);
        } else {
            return utcSecond.get().format(date);
        }
    }

    /**
     * Parses text from the beginning of the given string to produce a date.
     * 
     * @param date
     *            a string to parse
     * @return A Date parsed from the string.
     */
    public static Date parseUTC(String date) {
        try {
            return parse(date);
        } catch (ParseException pExc) {
            LOGGER.error("while parsing date " + date, pExc);
        }
        return null;
    }

    public static Date parse(String date) throws ParseException {
        if (date == null)
            return null;
        if (date.contains("T") && date.endsWith("Z"))
            return utcSecond.get().parse(date);
        return utcDay.get().parse(date);
    }

    /**
     * Returns the most likely granularity of a date. If hour, minute, second and millisecond of the date are zero, the day granularity is returned.
     * 
     * @param date
     *            date to check
     * @return the most likely granularity of the date
     */
    public static Granularity getGranularity(Date date) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTime(date);
            if (calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0
                    && calendar.get(Calendar.MILLISECOND) == 0) {
                return Granularity.YYYY_MM_DD;
            }
            return Granularity.YYYY_MM_DD_THH_MM_SS_Z;
        }
    }

    /**
     * Returns the granularity of a UTC date.
     * 
     * @param date
     * @return the granularity of the given date
     * @throws ParseException
     *             if the date is not in UTC format
     */
    public static Granularity getGranularity(String date) throws ParseException {
        parse(date);
        if (date.contains("T") && date.contains("Z")) {
            return Granularity.YYYY_MM_DD_THH_MM_SS_Z;
        }
        return Granularity.YYYY_MM_DD;
    }

    /**
     * Returns the current date.
     * 
     * @return date at current time
     */
    public static Date getCurrentDate() {
        Calendar cal = CALENDAR;
        synchronized (cal) {
            cal.setTime(new Date(System.currentTimeMillis()));
            return cal.getTime();
        }
    }


    /**
     * Returns the last second of the specified date.
     *
     * @param date Date to calculate end of day from
     * @return Last second of <code>date</code>
     */
    public static Date endOfDay(Date date) {
        Calendar cal = CALENDAR;
        synchronized (cal) {
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MINUTE, 59);
            return cal.getTime();            
        }
    }

    /**
     * Returns a new Date with the hours, milliseconds, seconds and minutes
     * set to 0.
     *
     * @param date Date used in calculating start of day
     * @return Start of <code>date</code>
     */
    public static Date startOfDay(Date date) {
        Calendar calendar = CALENDAR;
        synchronized(calendar) {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            return calendar.getTime();
        }
    }

    /**
     * Returns the day after <code>date</code>.
     *
     * @param date Date used in calculating next day
     * @return Day after <code>date</code>.
     */
    public static Date nextDay(Date date) {
        return addDays(date, 1);
    }

    /**
     * Returns the day before <code>date</code>.
     *
     * @param date Date used in calculating previous day
     * @return Day before <code>date</code>.
     */
    public static Date previousDay(Date date) {
        return addDays(date, -1);
    }

    /**
     * Adds <code>amount</code> days to <code>time</code> and returns
     * the resulting time. A negative amount is allowed.
     *
     * @param date used in calculating days
     * @param amount Amount of increment.
     * 
     * @return the <var>time</var> + <var>amount</var> days
     */
    public static Date addDays(Date date, int amount) {
        Calendar calendar = CALENDAR;
        synchronized(calendar) {
            calendar.setTimeInMillis(date.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, amount);
            return calendar.getTime();
        }
    }

    /**
     * Adds <code>amount</code> seconds to <code>time</code> and returns
     * the resulting time.
     *
     * @param date used in calculating seconds
     * @param amount Amount of increment.
     * 
     * @return the <var>time</var> + <var>amount</var> days
     */
    public static Date addSeconds(Date date, int amount) {
        Calendar calendar = CALENDAR;
        synchronized(calendar) {
            calendar.setTimeInMillis(date.getTime());
            calendar.add(Calendar.SECOND, amount);
            return calendar.getTime();
        }
    }

    /**
     * Return the former date.
     * 
     * @param dates array of dates
     * @return the former date or null if all arguments are null
     */
    public static Date min(Date... dates) {
        Date minDate = null;
        for(Date d : dates) {
            if(minDate == null) {
                minDate = d;
            } else if(d != null && d.compareTo(minDate) < 0) {
                minDate = d;
            }
        }
        return minDate;
    }

    /**
     * Return the later date.
     * 
     * @param dates array of dates
     * @return the latest date or null if all arguments are null
     */
    public static Date max(Date... dates) {
        Date maxDate = null;
        for(Date d : dates) {
            if(maxDate == null) {
                maxDate = d;
            } else if(d != null && d.compareTo(maxDate) > 0) {
                maxDate = d;
            }
        }
        return maxDate;
    }

    /**
     * Checks if a date is between from and until. If from or until are null, then they are
     * defined as infinity.
     * 
     * @param isBetween
     * @param from
     * @param until
     * @return
     */
    public static boolean between(Date isBetween, Date from, Date until) {
        if(isBetween == null)
            return false;

        if((from == null || isBetween.getTime() >= from.getTime()) &&
           (until == null || isBetween.getTime() <= until.getTime()))
            return true;
        return false;
    }

}
