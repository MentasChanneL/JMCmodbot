package com.prikolz;

import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import com.prikolz.automod.CheckCheater;
import com.prikolz.ds.DSEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.util.*;

public class run {
    public static Automod automod;
    public static Client client;

    private static final String IP = "137.74.4.178";
    //private static final String IP = "localhost";
    private static final int PORT = 25565;
    private static final String NICKNAME = "2M3V";
    public static final File DATABASE = new File("C:/Users/Сыр/Desktop/кринжМС/jmcmod.txt");
    public static boolean stopBuffer = false;
    public static boolean enableLS = true;
    public static JDA dsBot;

    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);

        Timer consoleListener = new Timer();
        consoleListener.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String command = scanner.nextLine();
                try {
                    commandHandler(command);
                }catch (Exception e) {
                    System.out.println( e.getMessage() );
                }
            }
        }, 0, 1);

        System.out.println(" - Razdacha bonus BOT - ");

        System.out.println("IP= " + IP + " PORT= " + PORT + " NICKNAME= " + NICKNAME);

        System.out.println("Console commands list:");
        System.out.println(">connect - connect/reconnect to server");
        System.out.println(">disconnect - disconnect from server");
        System.out.println(">pardon [nickname] - unmute player");
        System.out.println(">commands {number} - commands list from server");
        System.out.println(">tab {number} - players list\n");
        System.out.println(">reports - cheaters reports list\n");

        dsBot = JDABuilder.createDefault("")
                .addEventListeners( new DSEvents() )
                .enableIntents( GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS) )
                .build();
        dsBot.awaitReady();

        client = new Client(IP, PORT, NICKNAME);
        automod = new Automod(client.session);

    }

    public static void commandHandler(String command) {
        if (command.startsWith(">")) {
            String[] args = command.split(" ");
            String id = args[0].substring(1);

            if(id.equals("connect")) {
                try {
                    client = new Client(IP, PORT, NICKNAME);
                }catch (Exception e) { System.out.println( e.getMessage() ); }
                return;
            }

            if(id.equals("disconnect")) {
                try {
                    client.session.disconnect("by console");
                }catch (Exception e) { System.out.println( e.getMessage() ); }
                return;
            }

            if(id.equals("pardon")) {
                if(args.length < 2) {
                    System.out.println("Usage: >pardon [name]");
                    return;
                }
                try {
                    int pardon = automod.violations.get(args[1]) - 1;
                    automod.violations.put(args[1], pardon);
                    System.out.println("Убрано 1 нарушение у " + args[1] + ". Осталось: " + pardon);
                    automod.writeJson();
                }catch (Exception e) { System.out.println( e.getMessage() );}
                return;
            }

            if(id.equals("commands")) {
                int limit = 3;

                try{
                    limit = Integer.parseInt(args[1]);
                }catch (Exception ignore){}

                String line = "";
                int counter = 0;
                for(String c : client.SessionCOMMANDS) {
                    line = line + "/" + c + " ";
                    counter++;
                    if(counter == limit) {
                        System.out.println(line);
                        counter = 0;
                        line = "";
                    }
                }
                return;
            }

            if(id.equals("tab")) {
                int limit = 3;

                try{
                    limit = Integer.parseInt(args[1]);
                }catch (Exception ignore){}

                String line = "";
                int counter = 0;
                String display;
                for(Team team : client.SessionTeams.values()) {
                    for(String player : team.players) {
                        display = team.prefix + player + team.suffix;
                        line = line + display + "       ";
                        counter++;
                        if (counter == limit) {
                            System.out.println(line);
                            counter = 0;
                            line = "";
                        }
                    }
                }

                return;
            }

            if(id.equals("dos")) {
                if(args.length < 4) {
                    System.out.println("Usage: >dos [name] [content] [count]");
                    return;
                }

                int c = 1;
                try{
                    c = Integer.parseInt(args[3]);
                }catch (Exception ignore){}

                while (c > 1) {
                    automod.sendCommand("msg " + args[1] + " " + args[2].replace("_", " "));
                    c--;
                }

                return;
            }

            if(id.equals("data")) {
                if(args.length < 2) {
                    System.out.println("Usage: >data [name]/all");
                    return;
                }

                if(args[1].equals("all")) {
                    System.out.println(" |-----------------------| ");
                    for(String player : automod.data.keySet()) {
                        System.out.println("  ");
                        System.out.println(player + " DATA:");
                        System.out.println( automod.data.get(player).toString() );
                    }
                    System.out.println(" |-----------------------| ");
                    return;
                }

                if(automod.data.containsKey(args[1])) {
                    System.out.println(args[1] + " DATA:");
                    System.out.println( automod.data.get(args[1]).toString() );
                    return;
                }

                System.out.println("no player");

                return;
            }

            if(id.equals("buffer")) {
                if(args.length < 2) {
                    System.out.println("Usage: >buffer [on - 1/off - 0/clear - c]");
                    return;
                }

                if(args[1].equals("1")) {
                    run.stopBuffer = false;
                    System.out.println("Command buffer ON");
                    return;
                }
                if(args[1].equals("0")) {
                    run.stopBuffer = true;
                    System.out.println("Command buffer OFF");
                    return;
                }
                if(args[1].equals("c")) {
                    run.stopBuffer = true;
                    automod.commandBuffer.clear();
                    System.out.println("Command buffer CLEARED");
                    return;
                }

                return;
            }

            if(id.equals("reports")) {

                System.out.println("Текущие репорты: " + CheckCheater.reports);

                return;
            }

            System.out.println("Unknown console command");

            return;
        }
        if (command.startsWith("/")) {
            automod.sendCommand( command.substring(1) );
            return;
        }
        client.session.send(new ServerboundChatPacket(command, System.currentTimeMillis(), 0L, null, 0, new BitSet()));
    }

}