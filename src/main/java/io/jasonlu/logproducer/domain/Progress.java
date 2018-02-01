package io.jasonlu.logproducer.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Progress {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");

    private final int totalExpected;

    private int count;
    private LocalDateTime begin;
    private long beginNano;
    private long usedNanos;
    private String id = UUID.randomUUID().toString();

    public Progress(int totalExpected) {
        this.totalExpected = totalExpected;
    }

    public boolean isRunning() {
        return count > 0 && count < totalExpected;
    }

    public boolean isFinished() {
        return count == totalExpected;
    }

    public void update(int count, long updateTime) {
        if (this.count == 0 && count > 0) {
            this.beginNano = updateTime;
            this.begin = LocalDateTime.now();
        }

        this.count = count;
        usedNanos = updateTime - beginNano;
    }

    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public String getBeginAt() {
        return begin == null ? "" : begin.format(DATE_TIME_FORMATTER);
    }

    public String getEndAt() {
        return isFinished() ? begin.plusNanos(usedNanos).format(DATE_TIME_FORMATTER) : "";
    }

    public int getTotalExpected() {
        return totalExpected;
    }

    public long getUsedNanos() {
        return usedNanos;
    }
}
