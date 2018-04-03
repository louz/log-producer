package io.jasonlu.logproducer.core;

import io.jasonlu.logproducer.core.printer.SystemOutPrinter;
import io.jasonlu.logproducer.domain.Progress;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class EnginePerformence {

    private static final long NANO_500000 = 500000L;

    private Engine engine = new Engine();

    /* Annotations in Junit5
    @ParameterizedTest
    @CsvSource({ "10, 100", "100, 1000", "1000, 10000" })
    */
//
//    @Before
//    public void setUp() {
//        engine.setPrinter(new SystemOutPrinter());
//    }

    @Test
    @Parameters({ "10, 100", "1000, 10000", "100000, 1000000" })
    public void testExecDoneInTime(int logsPerSecond, int total) {
        Progress progress = new Progress(total);
        Printer printer = new SystemOutPrinter();
        engine.exec(printer, logsPerSecond, total, 100, progress);

        // 耗时为total - 1 次 正负 NANO_500000 纳秒误差以内
        assertTrue(progress.getUsedNanos() > (total - 1) * Engine.NANO_PER_SECOND / logsPerSecond - NANO_500000 && progress.getUsedNanos() <= total * Engine.NANO_PER_SECOND / logsPerSecond + NANO_500000);
    }

}
