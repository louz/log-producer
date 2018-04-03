package io.jasonlu.logproducer.core.printer;

import io.jasonlu.logproducer.core.Printer;
import org.springframework.stereotype.Component;

@Component("systemOutPrinter")
public class SystemOutPrinter implements Printer {
    @Override
    public void println(String s) {
        System.out.println(s);
    }
}
