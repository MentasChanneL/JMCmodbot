package com.prikolz;

import java.util.ArrayList;
import java.util.List;

public class DisplayNick {
    final static List<String> roles = new ArrayList<>();

    public String role = "";
    public String name = "";
    public String suffix = "";
    public String message = "";

    public DisplayNick() {
        if(!roles.isEmpty()) return;
        roles.add("Admin");
        roles.add("Mod");
        roles.add("Dev");
        roles.add("Support");
        roles.add("Nova");
        roles.add("Galaxy");
        roles.add("Star");
        roles.add("Planet");
        roles.add("Moon");
        roles.add("Meteor");
    }

    public void fromGlobalMessage(String msg) {
        String[] args = msg.split(":");
        this.message = args[1];
        String[] info = args[0].split(" ");
        if(info.length == 2) {
            this.name = info[1];
        }
        if(info.length == 3) {
            if(roles.contains(info[1])) {
                this.role = info[1];
                this.name = info[2];
            }else {
                this.name = info[1];
                this.suffix = info[2];
            }
        }
        if(info.length == 4) {
            this.role = info[1];
            this.name = info[2];
            this.suffix = info[3];
        }

    }

    public void fromPrivateMessage(String msg) {
        int index = msg.indexOf(")");
        this.message = msg.substring(index + 2);
        String change = msg.substring(0, index);
        index = change.indexOf("→");
        change = msg.substring(1, index - 1);

        String[] info = change.split(" ");
        if(info.length == 1) {
            this.name = info[0];
        }
        if(info.length == 2) {
            if(roles.contains(info[0])) {
                this.role = info[0];
                this.name = info[1];
            }else {
                this.name = info[0];
                this.suffix = info[1];
            }
        }
        if(info.length == 3) {
            this.role = info[0];
            this.name = info[1];
            this.suffix = info[2];
        }
    }

}
