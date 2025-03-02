package de.codelix.emsbridge.metrics.impl;

import com.influxdb.annotations.Column;
import com.influxdb.client.domain.WritePrecision;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.metrics.EventMetric;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.Instant;
import java.util.UUID;

public class DistanceTraveledMetric extends EventMetric {
    @Override
    public void enable() {
        EMSBridge.INSTANCE.getServer().getPluginManager().registerEvents(this, EMSBridge.INSTANCE);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        Integer entityId = EMSBridge.INSTANCE.getEntityService().getEntityIdNullableLocal(playerUuid);
        if (entityId == null) return;
        Location from = event.getFrom();
        Location to = event.getTo();
        double distance = from.distance(to);
        Measurement measurement = new Measurement(Instant.now(), entityId, distance);
        EMSBridge.INFLUX.writeMeasurement(WritePrecision.NS, measurement);
    }

    @com.influxdb.annotations.Measurement(name = "distance_traveled")
    public record Measurement(
            @Column(timestamp = true) Instant time,
            @Column(tag = true) int entity_id,
            @Column double distance
    ) {}
}
