package com.prikolz;

import java.util.ArrayList;
import java.util.List;

public class Team {
    String prefix = "";
    String suffix = "";

    List<String> players = new ArrayList<>();

    public String toString() {
        return ": prefix=" + prefix + ", suffix=" + suffix + ", players=" + players;
    }

}
