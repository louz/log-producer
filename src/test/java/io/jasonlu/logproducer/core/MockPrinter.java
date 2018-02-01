package io.jasonlu.logproducer.core;

import java.util.ArrayList;
import java.util.List;

public class MockPrinter implements Printer {

    private List<String> lines = new ArrayList<>();

    @Override
    public void println(String s) {
        lines.add(s);
    }

    public List<String> getLines() {
        return lines;
    }
}
