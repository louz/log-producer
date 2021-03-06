package io.jasonlu.logproducer.service;

import io.jasonlu.logproducer.core.Engine;
import io.jasonlu.logproducer.core.Printer;
import io.jasonlu.logproducer.domain.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private Engine engine;

    public void produce(Printer printer, int logPerSecond, int total, int length, Progress progress) {
        Thread logThread = new Thread(() -> engine.exec(printer, logPerSecond, total, length, progress));

        logThread.start();
    }
}
