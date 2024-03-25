package com.prikolz.lscommands;

import com.prikolz.DisplayNick;
import com.prikolz.run;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class LSHandler {

    public static long prikolcd = 0;

    public static void analys(String msg) {
        DisplayNick displayNick = new DisplayNick();
        displayNick.fromPrivateMessage(msg);
        String command = displayNick.message.toLowerCase();
        if(command.equals("анекдот"))  {
            sayPrikol(displayNick);
            return;
        }
        if(command.equals("команды"))  {
            say(displayNick.name, "Список команд: анекдот, команды");
            return;
        }
        if(command.equals("репис"))  {
            say(displayNick.name, "пепис");
            return;
        }
        if(command.equals("пепис"))  {
            say(displayNick.name, "репис");
            return;
        }

        List<String> what = Arrays.asList("понял", "что", "чего", "каво", "ладно", "не пон", "всм", "я бот", "окей", "нет", "круто", "ихихи");

        say( displayNick.name, what.get( new Random().nextInt(what.size()) ));

    }

    private static void sayPrikol(DisplayNick displayNick) {
        if(System.currentTimeMillis() < prikolcd) {
            return;
        }
        try {
            prikolcd = System.currentTimeMillis() + 1000;
            URL url = new URL("https://randstuff.ru/joke/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder content = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            connection.disconnect();

            line = content.substring(content.indexOf("id=\"joke\"><table class=\"text\"><tr><td>"));
            line = line.substring( line.indexOf("<td>") + 4, line.indexOf("</td>") );

            say(displayNick.name, "☻ " + line + " ☻");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void say(String getter, String content) {
        run.automod.sendCommand("msg " + getter + " " + content);
    }

}
