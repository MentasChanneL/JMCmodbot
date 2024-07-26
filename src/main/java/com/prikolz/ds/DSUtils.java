package com.prikolz.ds;

import com.prikolz.run;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DSUtils {

    private static final HashMap<String, String> formates = formatter();
    private static final List<String> chatBuffer = new ArrayList<>();
    private static long chatRate = 0;

    private static HashMap<String, String> formatter() {
        HashMap<String, String> result = new HashMap<>();

        result.put("\\[90m", "\\[0;30m");
        result.put("\\[91m", "\\[0;31m");
        result.put("\\[92m", "\\[0;32m");
        result.put("\\[93m", "\\[0;33m");
        result.put("\\[94m", "\\[0;34m");
        result.put("\\[95m", "\\[0;35m");
        result.put("\\[96m", "\\[0;36m");
        result.put("\\[97m", "\\[0;37m");

        return result;
    }

    public static void sendChatMessage(String content) {
        chatBuffer.add(handleMsg(content));
        if(chatRate > System.currentTimeMillis()) {
            return;
        }
        chatRate = System.currentTimeMillis() + 1000;
        StringBuilder result = new StringBuilder();
        for(String line : chatBuffer) {
            result.append(line).append("\n");
        }
        chatBuffer.clear();
        run.dsBot.getChannelById(TextChannel.class, DSBotConfig.chatChannel).sendMessage(MessageCreateData.fromContent( result.toString() )).queue();
    }
    private static String handleMsg(String content) {
        String result = content;
        for(String key : formates.keySet()) {
            result = result.replaceAll(key, formates.get(key));
        }
        return "```ansi\n" + result + "\n```";
    }

    public static void sendInfo(boolean isBan, String nick, String reason, int minutes, String[] data) {
        EmbedBuilder builder = new EmbedBuilder();
        StringBuilder datalist = new StringBuilder();
        for(String line : data) {
            datalist.append("- ```").append(line).append("```\n");
        }
        builder.setDescription("# " + nick + "\n## " + minutes + " " + reason + "\n- ```Данные:```\n" + datalist);
        builder.setColor(Color.RED);
        if(isBan) {
            run.dsBot.getChannelById(TextChannel.class, DSBotConfig.banChannel).sendMessage(MessageCreateData.fromEmbeds(builder.build())).queue();
            return;
        }
        run.dsBot.getChannelById(TextChannel.class, DSBotConfig.muteChannel).sendMessage(MessageCreateData.fromEmbeds(builder.build())).queue();
    }

    public static void sendReport(String owner, String target, String reason) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.ORANGE);
        builder.setDescription("Репорт на игрока **" + target + "** от игрока " + owner + " | **" + reason + "**");
        run.dsBot.getChannelById(TextChannel.class, DSBotConfig.reportChannel).sendMessage(MessageCreateData.fromEmbeds(builder.build())).queue();
    }

    public static String unixToTime(String format, long unix) {
        Date date = new Date(unix);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
