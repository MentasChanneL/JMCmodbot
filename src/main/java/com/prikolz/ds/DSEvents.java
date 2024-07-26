package com.prikolz.ds;

import com.prikolz.run;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.http.HttpRequestEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;


public class DSEvents implements EventListener {
    @Override
    public void onEvent(GenericEvent event) {
        onMessage(event);
        HttpRequestEvent(event);
        System.out.println(event);
    }

    private static void onMessage(GenericEvent e) {
        if(!(e instanceof MessageReceivedEvent event)) return;
        if(!event.getChannel().getId().equals(DSBotConfig.chatChannel)) return;
        if(event.getAuthor().getId().equals("1185977384323792967")) return;
    }

    private static void HttpRequestEvent(GenericEvent e) {
        if(!(e instanceof HttpRequestEvent event)) return;
    }
}
