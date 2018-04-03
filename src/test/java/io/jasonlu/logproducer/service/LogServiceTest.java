package io.jasonlu.logproducer.service;

import io.jasonlu.logproducer.Application;
import io.jasonlu.logproducer.core.printer.SystemOutPrinter;
import io.jasonlu.logproducer.domain.Progress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
// Mock表示使用模拟的应用服务器，默认值就是这个
@SpringBootTest(classes = Application.class)
public class LogServiceTest {

    @Autowired
    private LogService logService;

    @Test
    public void testProduce() throws InterruptedException {
        int logPerSecond = 1;
        int total = 10;
        Progress progress = new Progress(total);

        assertFalse(progress.isRunning());
        logService.produce(new SystemOutPrinter(), logPerSecond, total, 100, progress);

        Thread.sleep(1000);
        assertTrue(progress.isRunning());

        Thread.sleep(10000);
        assertTrue(progress.isFinished());
    }
}
