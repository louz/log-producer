package io.jasonlu.logproducer.core;

import io.jasonlu.logproducer.domain.Progress;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by jiehenglu on 18/01/24.
 */
public class EngineTest {

    private Engine engine = new Engine();

    @Before
    public void setUp() {
        engine.setPrinter(new SystemOutPrinter());
    }

    @Test
    public void testExecWithProgressObserver() {
        int logsPerSecond = 1;
        int total = 10;
        Progress process = new Progress(total);

        assertFalse(process.isRunning());
        engine.exec(logsPerSecond, total, 100, process);
        assertTrue(process.isFinished());
    }

    @Test
    public void testExecOutput() {
        int total = 2;
        int length = 10;
        Progress progress = new Progress(total);

        MockPrinter printer = new MockPrinter();
        engine.setPrinter(printer);

        engine.exec(1, total, length, progress);

        List<String> resultLines = printer.getLines();
        assertThat(resultLines.size(), Is.is(total));

        String[] tokens = StringUtils.split(resultLines.get(0), Engine.SPLITER);
        assertThat(tokens[1].length(), Is.is(length));

        long begin = parseTime(resultLines.get(0));
        long end = parseTime(resultLines.get(resultLines.size() - 1));

        long usedTime = end - begin;

        System.out.println("begin: " + begin + ", end: " + end);

        assertEquals(progress.getUsedNanos(), usedTime);
    }

    /**
     * 每行的格式为 beginNano|..............
     *
     * @param resultLine
     * @return
     */
    private long parseTime(String resultLine) {
        String[] token = StringUtils.split(resultLine, Engine.SPLITER);
        return Long.parseLong(token[0]);
    }
}
