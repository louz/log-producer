package io.jasonlu.logproducer.core;

import io.jasonlu.logproducer.domain.Progress;
import org.springframework.stereotype.Service;

/**
 * Created by jiehenglu on 18/01/24.
 */
@Service
public class Engine {
    public static final long NANO_PER_SECOND = 1000000000L;
    public static final String SPLITER = "|";

    private static final long NANO_PER_MILLISECCOND = 1000000L;

    public void exec(Printer printer, int logsPerSecond, int total, int length, Progress progress) {
        String fixedString = buildFixedString(length);

        long begin = System.nanoTime();
        long logIntervalInNanoSecond = NANO_PER_SECOND / logsPerSecond;

        for (int i = 0; i < total; i++) {
            long untilNanoTime = begin + i * logIntervalInNanoSecond;
            waitUntil(untilNanoTime);
            long now = System.nanoTime();

            StringBuilder sb = new StringBuilder(length + 20)
                    .append(now)
                    .append(SPLITER)
                    .append(fixedString);
            printer.println(sb.toString());

            progress.update(i + 1, now);
        }
    }

    private String buildFixedString(int length) {
        StringBuilder fixedStringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            fixedStringBuilder.append(".");
        }
        return fixedStringBuilder.toString();
    }

    private void waitUntil(long untilNanoTime) {
        if (System.nanoTime() >= untilNanoTime) {
            return;
        }

        sleepUntilLastMilliSecond(untilNanoTime);

        while (System.nanoTime() < untilNanoTime) {
            // do nothing
        }
    }

    /**
     * 当前时间与指定时间相差超过1ms时，采取sleep，否则会占用大量cpu资源做死循环
     *
     * @param untilNanoTime 指定时间（纳秒）
     */
    private void sleepUntilLastMilliSecond(long untilNanoTime) {
        long current = System.nanoTime();
        if (untilNanoTime - current > NANO_PER_MILLISECCOND) {
            try {
                Thread.sleep((untilNanoTime - current) / NANO_PER_MILLISECCOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

}
