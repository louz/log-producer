package io.jasonlu.logproducer.core;

import org.springframework.stereotype.Component;

@Component
public class SystemOutPrinter implements Printer {
    @Override
    public void println(String s) {
        System.out.println(s);
    }
}
