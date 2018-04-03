package io.jasonlu.logproducer.core.printer;

import io.jasonlu.logproducer.core.Printer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("loggerPrinter")
public class LoggerPrinter implements Printer {

    private Logger logger = LoggerFactory.getLogger(LoggerPrinter.class);

    @Override
    public void println(String s) {
        logger.info(s);
    }
}
