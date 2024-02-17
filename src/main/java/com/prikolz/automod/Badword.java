package com.prikolz.automod;

public class Badword {
    String comment;
    int initialMinutes;
    public Badword(String reason, int minutes) {
        comment = reason;
        initialMinutes = minutes;
    }
}
