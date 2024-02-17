package com.prikolz;

import com.github.steveice10.mc.protocol.data.game.ArgumentSignature;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;

import java.io.File;
import java.util.*;

public class run {
    public static Automod automod;
    public static Client client;

    private static final String IP = "137.74.4.178";
    private static final int PORT = 25565;
    private static final String NICKNAME = "2M3V";
    public static final File DATABASE = new File("C:/Users/Сыр/Desktop/кринжМС/jmcmod.txt");

    public static void main(String[] args) {

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

            System.out.println("Unknown console command");

            return;
        }
        if (command.startsWith("/")) {
            List<ArgumentSignature> signs = new ArrayList<>();
            client.session.send(new ServerboundChatCommandPacket(
                    command.substring(1),
                    System.currentTimeMillis(),
                    0L,
                    signs,
                    0,
                    new BitSet()
                    )
            );
            return;
        }
        client.session.send(new ServerboundChatPacket(command, System.currentTimeMillis(), 0L, null, 0, new BitSet()));
    }

}