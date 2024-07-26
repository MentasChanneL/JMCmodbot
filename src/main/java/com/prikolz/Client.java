package com.prikolz;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.game.command.CommandNode;
import com.github.steveice10.mc.protocol.data.game.command.CommandType;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamAction;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundCommandsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard.ClientboundSetPlayerTeamPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import com.prikolz.automod.users.Users;
import com.prikolz.ds.DSUtils;
import com.prikolz.lscommands.LSHandler;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.*;

import static com.prikolz.run.automod;

public class Client {
    public Session session;
    public List<String> SessionCOMMANDS;
    public HashMap<String, Team> SessionTeams;

    public Client(String host, int port, String name) {
        MinecraftProtocol protocol = new MinecraftProtocol(name);
        SessionTeams = new HashMap<>();

        session = new TcpClientSession(host, port, protocol, null);

        session.addListener(new SessionAdapter() {
            @Override
            public void packetReceived(Session session, Packet packet) {

                if (packet instanceof ClientboundLoginPacket) {
                    System.out.println("Login");
                    //session.send(new ServerboundChatPacket(
                    //        "ᴀᴜᴛᴏᴍᴏᴅ v240302. Лимиты: 3 рекламы в 30 минут, 2 одинаковых сообщения, 0 вертолётов. Ведите себя адекватно. ЛС Команды: анекдот",
                    //        System.currentTimeMillis(),
                    //        0L,
                    //        null,
                    //        0,
                    //        new BitSet()
//
                    //));
                    //session.send(new ServerboundChatPacket(
                    //        "бип бип боп",
                    //        System.currentTimeMillis() + 2000,
                    //        0L,
                    //        null,
                    //        0,
                    //        new BitSet()
//
                    //));
                    return;
                }

                if (packet instanceof ClientboundSystemChatPacket pm) {
                    String message = PlainTextComponentSerializer.plainText().serialize(pm.getContent());
                    String ansii = ANSIComponentSerializer.ansi().serialize(pm.getContent());
                    System.out.println( ansii );
                    DSUtils.sendChatMessage( ansii );
                    if(message.startsWith("[!]")) {
                        automod.mod(message);
                        return;
                    }
                    if(run.enableLS && message.startsWith("(") && !(message.startsWith("(Ты"))) {
                        LSHandler.analys(message);
                        return;
                    }
                    if(!Users.isOpen) {
                        Users.grabMessage(message);
                        return;
                    }
                    return;
                }

                if (packet instanceof ClientboundCommandsPacket pm) {
                    String[] el;
                    SessionCOMMANDS = new ArrayList<>();
                    for(CommandNode e : pm.getNodes()) {
                        if(e.getType().equals(CommandType.ROOT) || e.getType().equals(CommandType.LITERAL)) {
                            try {
                                el = e.getName().split(":");
                                SessionCOMMANDS.add(el[el.length - 1]);
                            } catch (Exception ex) {
                                if (e.getName() == null) continue;
                                if (e.getName().equals("null")) continue;
                                SessionCOMMANDS.add(e.getName());
                            }
                        }
                    }
                    return;
                }

                if (packet instanceof ClientboundSetPlayerTeamPacket pm) {

                    if( pm.getAction().equals(TeamAction.CREATE)) {
                        if(SessionTeams.containsKey(pm.getTeamName())) return;
                        SessionTeams.put(pm.getTeamName(), new Team());
                    }

                    if(pm.getAction().equals(TeamAction.UPDATE)) {
                        Team team = SessionTeams.get(pm.getTeamName());
                        if(team == null) {
                            team = new Team();
                        }
                        if(pm.getPrefix() != null) {
                            team.prefix = ANSIComponentSerializer.ansi().serialize( pm.getPrefix() );
                        }
                        if(pm.getSuffix() != null) {
                            team.suffix = ANSIComponentSerializer.ansi().serialize( pm.getSuffix() );
                        }
                        SessionTeams.put(pm.getTeamName(), team);
                    }

                    if(pm.getAction().equals(TeamAction.ADD_PLAYER)) {
                        Team team = SessionTeams.get(pm.getTeamName());
                        team.players.addAll(Arrays.asList(pm.getPlayers()));
                        Set<String> set = new HashSet<>(team.players);
                        team.players.clear();
                        team.players.addAll(set);
                        SessionTeams.put(pm.getTeamName(), team);
                    }

                    if(pm.getAction().equals(TeamAction.REMOVE_PLAYER)) {
                        Team team = SessionTeams.get(pm.getTeamName());
                        team.players.removeAll(Arrays.asList(pm.getPlayers()));
                        SessionTeams.put(pm.getTeamName(), team);
                    }

                    if(pm.getAction().equals(TeamAction.REMOVE)) {
                        SessionTeams.remove(pm.getTeamName());
                    }

                }

            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                System.out.println("Disconnected: " + ANSIComponentSerializer.ansi().serialize( event.getReason() ));
                if (event.getCause() != null) {
                    event.getCause().printStackTrace();
                }
            }
        });

        session.connect();
    }

}
