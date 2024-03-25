package com.prikolz.automod;

import java.util.ArrayList;
import java.util.List;

public class SameMessages {
    public List<Long> times = new ArrayList<>();

    public boolean addTimes(long time, int violationCount, long interval) {
        int i = 0;
        for (long t : new ArrayList<>(this.times)) {
            if(System.currentTimeMillis() - t > interval) {
                this.times.remove(i);
                continue;
            }
            i++;
        }
        this.times.add(time);
        return this.times.size() >= violationCount;
    }

    @Override
    public String toString() {
        return "times = " + times;
    }
}
