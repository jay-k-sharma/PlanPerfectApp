package com;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlot(LocalTime start, LocalTime end) {
        this.startTime = start;
        this.endTime = end;
    }
    public LocalTime getStart() {
        LocalTime start = this.startTime;
        return start;
    }

    public LocalTime getEnd() {
        LocalTime end = this.endTime;
        return end;
    }
}




