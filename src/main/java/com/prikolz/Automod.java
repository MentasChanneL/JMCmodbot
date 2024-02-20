package com.prikolz;

import com.github.steveice10.mc.protocol.data.game.ArgumentSignature;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import com.github.steveice10.packetlib.Session;
import com.prikolz.automod.PlayerData;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.prikolz.run.DATABASE;

public class Automod {

    Session client;
    HashMap<String, PlayerData> data;
    HashMap<String, Integer> violations;
    final List<String> role;

    public Automod(Session client){
        this.client = client;
        data = new HashMap<>();
        violations = new HashMap<>();
        role = new ArrayList<>();
        role.add("Admin");
        role.add("Mod");
        role.add("Dev");
        role.add("Support");
        role.add("Nova");
        role.add("Galaxy");
        role.add("Star");
        role.add("Planet");
        role.add("Moon");
        role.add("Meteor");
        parseJson();
    }

    public void mod(String message) {
        String name = "*";
        String[] args = message.split(":");
        String[] info = args[0].split(" ");
        if(info.length == 2) {
            name = info[1];
        }
        if(info.length == 3) {
            if(role.contains(info[1])) {
                name = info[2];
            }else {
                name = info[1];
            }
        }
        if(info.length == 4) {
            name = info[2];
        }

        if(name.equals("*")) return;

        String msg = message.substring(message.indexOf(":") + 1);

        PlayerData messages = this.data.get(name);
        if(messages == null) {
            messages = new PlayerData();
        }
        messages.analyseMessage(msg);
        if(messages.isViolation) {
            messages.isViolation = false;
            mute(name, messages.violationMsg, messages.violationInstantMinutes);
        }
        this.data.put(name, messages);
    }

    public void parseJson() {
        try {
            FileReader fileReader = new FileReader(DATABASE);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String content = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(content);
            for(String key : jsonObject.keySet()) {
                violations.put(key, jsonObject.getInt(key));
            }
            System.out.println("История:");
            System.out.println(violations);

        }catch (Exception e) {System.out.println( e.getMessage() );}

    }

    public void mute(String name, String reason, int instanceMinutes) {
        int v = 0;
        if(!(violations.containsKey(name))) {
            violations.put(name, 1);
        }else{
            v = violations.get(name);
            violations.put(name, v + 1);
        }
        int minutes = (int) (instanceMinutes * Math.pow(2, v));
        List<ArgumentSignature> signs = new ArrayList<>();
        String command = "mute " + name + " " + minutes + "m [ ᴀᴜᴛᴏᴍᴏᴅ ] " + reason;
        System.out.println("⚠⚠⚠ Автоматический мут " + name + "! Команда: " + command);
        client.send(new ServerboundChatCommandPacket(
                        command,
                        System.currentTimeMillis(),
                        0L,
                        signs,
                        0,
                        new BitSet()
                )
        );
        command = "msg " + name + " Вы были автоматически заглушенны ботом ᴀᴜᴛᴏᴍᴏᴅ! Причина: " + reason + ". Подробнее об этом пункте правил, вы можете прочитать в /rules. Если ваш мут - это ошибка, то сообщите другому модератору, саппорту или в дискорд @2m3v!";
        client.send(new ServerboundChatCommandPacket(
                        command,
                        System.currentTimeMillis(),
                        0L,
                        signs,
                        0,
                        new BitSet()
                )
        );
        writeJson();
    }

    public void writeJson() {
        JSONObject jsonObject = new JSONObject(violations);
        try (FileWriter file = new FileWriter(DATABASE)) {
            file.write(jsonObject.toString());
            System.out.println("Нарушения записаны в файл.");
        } catch (IOException e) {
            System.out.println( e.getMessage() );
        }
    }

}
