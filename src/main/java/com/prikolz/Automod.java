package com.prikolz;

import com.github.steveice10.mc.protocol.data.game.ArgumentSignature;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import com.github.steveice10.packetlib.Session;
import com.prikolz.automod.BotChecker;
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
    HashMap<String, BotChecker> botChecker;
    List<String> commandBuffer;
    boolean commandBufferActive;
    long commandBufferCD;

    public Automod(Session client){
        this.client = client;
        data = new HashMap<>();
        violations = new HashMap<>();
        botChecker = new HashMap<>();
        commandBuffer = new ArrayList<>();
        commandBufferActive = false;
        commandBufferCD = 0;
        parseJson();
    }

    public void mod(String message) {
        DisplayNick displayNick = new DisplayNick();
        displayNick.fromGlobalMessage(message);
        String name = displayNick.name;
        if(name.isEmpty()) return;

        String msg = message.substring(message.indexOf(":") + 1);

        sendCommand("msg " + name + " Вы написали в глобальный чат:" + msg + ". Я заполнмнил это, чтобы использовать ваши сообщения в контр-аргументе против вас-же. Просмотреть свои сообщения можно через команду: анектод");

        PlayerData messages = this.data.get(name);
        if(messages == null) {
            messages = new PlayerData();
        }
        putBotChecker(msg.toLowerCase(), name);
        messages.analyseMessage(msg);
        if(messages.isViolation) {
            messages.isViolation = false;
            mute(name, messages.violationMsg, messages.violationInstantMinutes, true);
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

    public void mute(String name, String reason, int instanceMinutes, boolean pardon) {
        int v = 0;
        if(!(violations.containsKey(name))) {
            violations.put(name, 1);
        }else{
            v = violations.get(name);
            violations.put(name, v + 1);
        }
        int minutes = (int) (instanceMinutes * Math.pow(2, v));
        String command = "mute " + name + " " + minutes + "m [ᴀᴜᴛᴏᴍᴏᴅ] " + reason;
        System.out.println("⚠⚠⚠ Автоматический мут " + name + "! Команда: " + command);
        sendCommand(command);
        if(pardon) {
            command = "msg " + name + " Вы были автоматически заглушенны ботом ᴀᴜᴛᴏᴍᴏᴅ! Причина: " + reason + ". Подробнее об этом пункте правил, вы можете прочитать в /rules. Если ваш мут - это ошибка, то сообщите другому модератору, саппорту или в дискорд @2m3v!";
            sendCommand(command);
        }

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

    public void putBotChecker(String msg, String author) {
        BotChecker bot;
        if(!(botChecker.containsKey(msg))) {
            bot = new BotChecker();
            bot.counter = 1;
            bot.addTarget(author);
            botChecker.put(msg, bot);
            return;
        }
        bot = botChecker.get(msg);
        bot.counter++;
        bot.addTarget(author);
        if(bot.counter > 2) {
            System.out.println("⚠ Флуд | Счёт: " + bot.counter);
        }
        if(bot.targets.size() > 4) {
            for(String victim : bot.targets.keySet()) {
                mute(victim, "2.3 Участие в флуде", 20, false);
            }
            botChecker.remove(msg);
        }
        if(botChecker.size() > 50) {
            botChecker.remove(botChecker.keySet().stream().toList().getFirst());
        }
    }

    public void sendCommand(String command) {
        if(command.length() > 127) {
            this.commandBuffer.add(command.substring(0, 128));
        }else {
            this.commandBuffer.add(command);
        }
        if(!this.commandBufferActive) {
            this.commandBufferActive = true;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(commandBufferCD > System.currentTimeMillis()) return;
                    String command = commandBuffer.getFirst();
                    List<ArgumentSignature> signs = new ArrayList<>();
                    System.out.println("Буфер команд: " + commandBuffer.size());
                    client.send(new ServerboundChatCommandPacket(
                                    command,
                                    System.currentTimeMillis(),
                                    0L,
                                    signs,
                                    0,
                                    new BitSet()
                            )
                    );
                    commandBuffer.removeFirst();
                    if(commandBuffer.isEmpty()) {
                        commandBufferActive = false;
                        this.cancel();
                    }
                    commandBufferCD = System.currentTimeMillis() + 600;
                }
            }, 0, 700);
        }
    }

}
