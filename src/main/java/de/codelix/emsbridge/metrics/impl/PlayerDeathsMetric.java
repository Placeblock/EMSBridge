package de.codelix.emsbridge.metrics.impl;

import com.influxdb.annotations.Column;
import com.influxdb.client.domain.WritePrecision;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.metrics.EventMetric;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.time.Instant;
import java.util.UUID;

public class PlayerDeathsMetric extends EventMetric {
    @Override
    public void enable() {
        EMSBridge.INSTANCE.getServer().getPluginManager().registerEvents(this, EMSBridge.INSTANCE);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        NamespacedKey damageType = event.getDamageSource().getDamageType().getKey();
        UUID playerUuid = player.getUniqueId();
        Integer entityId = EMSBridge.INSTANCE.getEntityService().getEntityIdNullableLocal(playerUuid);
        if (entityId == null) return;
        Measurement measurement = new Measurement(Instant.now(), entityId, damageType, 1);
        EMSBridge.INFLUX.writeMeasurement(WritePrecision.NS, measurement);
    }

    @com.influxdb.annotations.Measurement(name = "player_deaths")
    public record Measurement(
            @Column(timestamp = true) Instant time,
            @Column(tag = true) int entity_id,
            @Column(tag = true) NamespacedKey damageType,
            @Column int count
    ) {}
}
