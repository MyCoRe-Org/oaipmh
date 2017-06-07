package org.mycore.oai.pmh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.Test;

public class DateUtilsTest {

    @Test
    public void formatUTCSecond() {
        assertEquals("2000-10-10T12:30:30Z", DateUtils.formatUTCSecond(secondSampleInstant()));
    }

    @Test
    public void formatUTCDay() {
        assertEquals("2000-10-10", DateUtils.formatUTCDay(secondSampleInstant()));
    }

    @Test
    public void format() {
        DateUtils.setGranularity(Granularity.YYYY_MM_DD_THH_MM_SS_Z);
        assertEquals("2000-10-10T12:30:30Z", DateUtils.format(secondSampleInstant()));
        DateUtils.setGranularity(Granularity.YYYY_MM_DD);
        assertEquals("2000-10-10", DateUtils.format(secondSampleInstant()));
    }

    @Test
    public void parse() {
        assertEquals(secondSampleInstant(), DateUtils.parse("2000-10-10T12:30:30Z"));
        assertEquals(daySampleInstant(), DateUtils.parse("2000-10-10"));
    }

    @Test
    public void guessGranularity() {
        assertEquals(Granularity.YYYY_MM_DD_THH_MM_SS_Z, DateUtils.guessGranularity(secondSampleInstant()));
        assertEquals(Granularity.YYYY_MM_DD, DateUtils.guessGranularity(daySampleInstant()));
    }

    @Test
    public void addDays() {
        Instant daySample = daySampleInstant();
        assertEquals(LocalDate.of(2000, 10, 12).atStartOfDay().toInstant(ZoneOffset.UTC),
            DateUtils.addDays(daySample, 2));
        assertEquals(LocalDate.of(2000, 10, 8).atStartOfDay().toInstant(ZoneOffset.UTC),
            DateUtils.addDays(daySample, -2));
    }

    @Test
    public void addSeconds() {
        Instant secondSample = secondSampleInstant();
        assertEquals(LocalDateTime.of(2000, 10, 10, 12, 30, 40).toInstant(ZoneOffset.UTC),
            DateUtils.addSeconds(secondSample, 10));
        assertEquals(LocalDateTime.of(2000, 10, 10, 12, 30, 20).toInstant(ZoneOffset.UTC),
            DateUtils.addSeconds(secondSample, -10));
    }

    @Test
    public void min() {
        assertEquals(Instant.ofEpochMilli(20),
            DateUtils.min(Instant.ofEpochMilli(50), Instant.ofEpochMilli(20), Instant.ofEpochMilli(1000)));
    }

    @Test
    public void max() {
        assertEquals(Instant.ofEpochMilli(1000),
            DateUtils.max(Instant.ofEpochMilli(50), Instant.ofEpochMilli(20), Instant.ofEpochMilli(1000)));
    }

    @Test
    public void between() {
        assertTrue(DateUtils.between(Instant.ofEpochMilli(500), Instant.ofEpochMilli(50), Instant.ofEpochMilli(1000)));
        assertFalse(
            DateUtils.between(Instant.ofEpochMilli(1500), Instant.ofEpochMilli(50), Instant.ofEpochMilli(1000)));
        assertFalse(DateUtils.between(Instant.ofEpochMilli(25), Instant.ofEpochMilli(50), Instant.ofEpochMilli(1000)));
    }

    private Instant daySampleInstant() {
        return LocalDate.of(2000, 10, 10).atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    private Instant secondSampleInstant() {
        return LocalDateTime.of(2000, 10, 10, 12, 30, 30).toInstant(ZoneOffset.UTC);
    }

}
