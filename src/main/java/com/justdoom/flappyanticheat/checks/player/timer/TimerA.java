package com.justdoom.flappyanticheat.checks.player.timer;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityTickEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.listener.PlayerPositionListener;
import net.minestom.server.network.packet.server.play.PlayerPositionAndLookPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerA extends Check {

    private Map<UUID, Long> lastTime = new HashMap<>();
    private Map<UUID, Double> balance = new HashMap<>();

    public TimerA() {
        super("Timer", "A", true);

        EventNode<Event> node = EventNode.all("demo");
        node.addListener(PlayerMoveEvent.class, event -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUuid();

            if (event.getPacketId() == PacketType.Play.Client.POSITION || e.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

                if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                    return;

                long time = System.currentTimeMillis();
                long lastTime = this.lastTime.getOrDefault(uuid, 0L) != 0 ? this.lastTime.getOrDefault(uuid, 0L) : time - 50;
                this.lastTime.put(uuid, time);

                long rate = time - lastTime;

                double balanceOrDefault = this.balance.getOrDefault(uuid, 0.0);
                this.balance.put(uuid, balanceOrDefault += 50.0);
                balanceOrDefault = this.balance.getOrDefault(uuid, 0.0);
                this.balance.put(uuid, balanceOrDefault -= rate);

                if(this.balance.getOrDefault(uuid, 0.0) >= 10.0){
                    fail("balance=" + balance, player);
                    this.balance.put(uuid, 0.0);
                }
            } else if (e.getPacketId() == PacketType.Play.Server.POSITION){
                double balanceOrDefault = this.balance.getOrDefault(uuid, 0.0);
                this.balance.put(uuid, balanceOrDefault -= 50.0);
            }
        });
    }
}
