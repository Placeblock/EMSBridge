package de.codelix.emsbridge.metrics.impl;

import com.influxdb.annotations.Column;
import com.influxdb.client.domain.WritePrecision;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.metrics.EventMetric;
import de.codelix.minecraftstatistics.MinecraftStatistics;
import de.codelix.minecraftstatistics.metrics.EventMetric;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.time.Instant;
import java.util.UUID;

public class BlocksMetric extends EventMetric {
    @Override
    public void enable() {
        EMSBridge.INSTANCE.getServer().getPluginManager().registerEvents(this, EMSBridge.INSTANCE);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        UUID playerUuid = event.getPlayer().getUniqueId();
        Material type = event.getBlock().getType();
        Measurement measurement = new Measurement(Instant.now(), playerUuid, type, false, 1);
        EMSBridge.INFLUX.writeMeasurement(WritePrecision.NS, measurement);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        UUID playerUuid = event.getPlayer().getUniqueId();
        Material type = event.getBlock().getType();
        Measurement measurement = new Measurement(Instant.now(), playerUuid, type, true, 1);
        EMSBridge.INFLUX.writeMeasurement(WritePrecision.NS, measurement);
    }

    @com.influxdb.annotations.Measurement(name = "broken_blocks")
    public record Measurement(
            @Column(timestamp = true) Instant time,
            @Column(tag = true) UUID player_uuid,
            @Column(tag = true) Material material,
            @Column(tag = true) boolean placed,
            @Column int amount
    ) {}
}
