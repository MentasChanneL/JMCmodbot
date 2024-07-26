package com.prikolz.automod.users;

import com.prikolz.run;

public class Users {
    public static boolean isOpen = true;
    public static String target = "";
    public static Runnable run;
    public static UserData data;

    public static boolean infoRequest(String target, Runnable run) {
        if(!isOpen) return false;
        Users.isOpen = false;
        Users.target = target; Users.run = run;
        com.prikolz.run.automod.sendCommand("users get-info " + target);
        return true;
    }

    public static void grabMessage(String message) {
        if(message.contains("┌──────── Информация об пользователе")) {
            data = new UserData();
            String[] lines = message.split("\n");
            for (String line : lines) {
                analysMessage(line);
            }
            isOpen = true;
            Users.run.run();
            System.out.println(" end ");
        }
    }

    private static void analysMessage(String plainText) {
        String[] args = plainText.split(" ");
        if(plainText.contains("Зарегистрирован")) {
            data.registrationDate = args[2];
            return;
        }
        if(plainText.contains("UUID:")) {
            data.UUID = args[2];
            return;
        }
        if(plainText.contains("Роли:")) {
            String[] args2 = plainText.split(":");
            data.Roles = args2[1];
            return;
        }
        if(plainText.contains("Имеет") && plainText.contains("сессий")) {
            data.sessions = Integer.parseInt(args[2]);
            return;
        }
    }
}
