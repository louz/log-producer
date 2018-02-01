package io.jasonlu.logproducer.domain;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.NANOS;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ProgressTest {

    @Test
    public void testGetBeginAt() {
        Progress progress = new Progress(10);
        assertThat(progress.getBeginAt(), Is.is(""));
    }

    @Test
    public void testGetBeginAtWhenRunning() {
        Progress progress = new Progress(10);
        progress.update(1, 1);
        assertTrue(progress.getBeginAt().length() == 29);
    }

    @Test
    public void testGetEndAt() {
        Progress progress = new Progress(10);
        assertThat(progress.getEndAt(), Is.is(""));
    }

    @Test
    public void testGetEndAtWhenFinished() {
        int totalExpected = 10;
        long beginNano = 1;
        long endNano = 10;

        Progress progress = new Progress(totalExpected);
        progress.update(1, beginNano);
        progress.update(totalExpected, endNano);
        assertTrue(progress.getEndAt().length() == 29);

        LocalDateTime begin = LocalDateTime.parse(progress.getBeginAt(), Progress.DATE_TIME_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(progress.getEndAt(), Progress.DATE_TIME_FORMATTER);
        assertThat(begin.until(end, NANOS), Is.is(endNano - beginNano));
        assertThat(progress.getUsedNanos(), Is.is(endNano - beginNano));
    }
}
