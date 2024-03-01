package com.prikolz.lscommands;

import com.github.steveice10.mc.protocol.data.game.ArgumentSignature;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
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

        List<ArgumentSignature> signs = new ArrayList<>();
        run.client.session.send(new ServerboundChatCommandPacket(
                        "msg " + displayNick.name + " " + Arrays.asList("че", "что", "чего", "каво", "чо", "не пон", "всм", "?").get(new Random().nextInt(8)),
                        System.currentTimeMillis(),
                        0L,
                        signs,
                        0,
                        new BitSet()
                )
        );

    }

    private static void sayPrikol(DisplayNick displayNick) {
        List<ArgumentSignature> signs = new ArrayList<>();
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

            run.client.session.send(new ServerboundChatCommandPacket(
                    "msg " + displayNick.name + " ☻ " + line + " ☻",
                            System.currentTimeMillis(),
                            0L,
                            signs,
                            0,
                            new BitSet()
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
