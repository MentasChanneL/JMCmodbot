package com.prikolz.ds;

import com.prikolz.run;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class DSUtils {
    public static void sendChatMessage(String content) {
        run.dsBot.getChannelById(TextChannel.class, DSBotConfig.chatChannel).sendMessage(MessageCreateData.fromContent( handleMsg(content) )).queue();
    }
    private static String handleMsg(String content) {

    }
}
