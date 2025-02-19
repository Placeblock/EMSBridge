package de.codelix.emsbridge.metrics.impl;

import com.influxdb.annotations.Column;
import com.influxdb.client.domain.WritePrecision;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.metrics.EventMetric;
import de.codelix.minecraftstatistics.MinecraftStatistics;
import de.codelix.minecraftstatistics.metrics.EventMetric;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;

import java.time.Instant;
import java.util.UUID;

public class EntityDeathsMetric extends EventMetric {
    @Override
    public void enable() {
        EMSBridge.INSTANCE.getServer().getPluginManager().registerEvents(this, EMSBridge.INSTANCE);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(EntityDeathEvent event) {
        if (event.isCancelled() || event.getEntity() instanceof Player) return;
        EntityType entityType = event.getEntityType();
        Entity causingEntity = event.getDamageSource().getCausingEntity();
        NamespacedKey damageType = event.getDamageSource().getDamageType().getKey();
        UUID causingPlayerUUID = null;
        if (causingEntity instanceof Player player) {
            causingPlayerUUID = player.getUniqueId();
        }
        Measurement measurement = new Measurement(Instant.now(), entityType, damageType, causingPlayerUUID, 1);
        EMSBridge.INFLUX.writeMeasurement(WritePrecision.NS, measurement);
    }

    @com.influxdb.annotations.Measurement(name = "entity_deaths")
    public record Measurement(
            @Column(timestamp = true) Instant time,
            @Column(tag = true) EntityType entityType,
            @Column(tag = true) NamespacedKey damageType,
            @Column(tag = true) UUID player_uuid,
            @Column int count
    ) {}
}
