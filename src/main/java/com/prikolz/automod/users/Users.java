package com.prikolz.automod.users;

import com.prikolz.run;
import net.dv8tion.jda.api.entities.User;

public class Users {
    public static boolean isOpen = true;
    public static String target = "";
    public static Runnable run;
    public static UserData data;

    public static boolean infoRequest(String target, Runnable run) {
        if(!isOpen) return false;
        Users.isOpen = false;
        Users.target = target; Users.run = run;
        data = new UserData();
        com.prikolz.run.automod.sendCommand("users get-info " + target);
        return true;
    }

    public static void grabMessage(String message) {
        if(message.startsWith("command.expected.separator\n... get-info")) {
            data.ERRORS = new String[]{"not_found"};
            isOpen = true;
            Users.run.run();
            return;
        }
        if(message.startsWith("Users » Пользователь") && message.endsWith("не найден.")) {
            data.ERRORS = new String[]{"not_found"};
            isOpen = true;
            Users.run.run();
            return;
        }
        if(message.contains("┌──────── Информация об пользователе")) {
            String[] lines = message.split("\n");
            for (String line : lines) {
                analysMessage(line);
            }
            isOpen = true;
            Users.run.run();
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
