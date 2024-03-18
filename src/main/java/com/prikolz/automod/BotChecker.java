package com.prikolz.automod;

import it.unimi.dsi.fastutil.Hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BotChecker {
    public int counter;
    public HashMap<String, Long> targets;

    public BotChecker(){
        this.targets = new HashMap<>();
        this.counter = 0;
    }

    public void addTarget(String nick){
        for(String i : new HashSet<>( this.targets.keySet() )) {
            long time = targets.get(i);
            if(time < System.currentTimeMillis()) {
                this.targets.remove(i);
            }
        }
        this.targets.put(nick, System.currentTimeMillis() + 30000);
    }

}
