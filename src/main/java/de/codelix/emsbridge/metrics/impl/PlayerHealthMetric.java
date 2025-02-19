package de.codelix.emsbridge.metrics.impl;

import com.influxdb.annotations.Column;
import com.influxdb.client.domain.WritePrecision;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.metrics.EventMetric;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Instant;
import java.util.UUID;

public class PlayerHealthMetric extends EventMetric {
    @Override
    public void enable() {
        EMSBridge.INSTANCE.getServer().getPluginManager().registerEvents(this, EMSBridge.INSTANCE);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled() ||
                !(event.getEntity() instanceof Player player)) return;
        UUID playerUuid = player.getUniqueId();
        double damage = event.getDamage();
        double newHealth = player.getHealth() - damage;
        Measurement measurement = new Measurement(Instant.now(), playerUuid, damage, newHealth);
        EMSBridge.INFLUX.writeMeasurement(WritePrecision.NS, measurement);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHeal(EntityRegainHealthEvent event) {
        if (event.isCancelled() ||
            !(event.getEntity() instanceof Player player)) return;
        UUID playerUuid = player.getUniqueId();
        double delta = event.getAmount();
        double newHealth = player.getHealth() + delta;
        Measurement measurement = new Measurement(Instant.now(), playerUuid, delta, newHealth);
        EMSBridge.INFLUX.writeMeasurement(WritePrecision.NS, measurement);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        double newHealth = player.getHealth();
        Measurement measurement = new Measurement(Instant.now(), playerUuid, 0, newHealth);
        EMSBridge.INFLUX.writeMeasurement(WritePrecision.NS, measurement);
    }

    @com.influxdb.annotations.Measurement(name = "player_health")
    public record Measurement(
            @Column(timestamp = true) Instant time,
            @Column(tag = true) UUID player_uuid,
            @Column double delta,
            @Column double health
    ) {}
}
