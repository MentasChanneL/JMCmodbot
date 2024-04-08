package com.prikolz.automod;

public class FludeCharacter {
    public char character;
    public int power;
    public long time;

    public FludeCharacter(char character, int power) {
        this.character = character;
        this.power = power;
        this.time = 0;
    }

    public FludeCharacter(char character, int power, long time) {
        this.character = character;
        this.power = power;
        this.time = time;
    }

    @Override
    public String toString() {
        return "(char = '" + character + "' power = " + power + " time = " + time + ")";
    }
}
