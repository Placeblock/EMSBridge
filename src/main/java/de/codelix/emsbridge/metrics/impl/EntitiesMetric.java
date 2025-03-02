package de.codelix.emsbridge.metrics.impl;

import com.influxdb.annotations.Column;
import com.influxdb.client.domain.WritePrecision;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.metrics.ScheduledMetric;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EntitiesMetric extends ScheduledMetric {
    private final Server server;

    public EntitiesMetric(Server server) {
        super(1000);
        this.server = server;
    }

    public List<Measurement> get() {
        final Instant now = Instant.now();
        List<Measurement> worldEntities = new ArrayList<>();
        for (World world : this.server.getWorlds()) {
            worldEntities.add(new Measurement(now, world.getName(), world.getEntityCount()));
        }
        return worldEntities;
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTask(EMSBridge.INSTANCE, () -> {
            List<EntitiesMetric.Measurement> values = this.get();
            Bukkit.getScheduler().runTaskAsynchronously(EMSBridge.INSTANCE, () ->
                EMSBridge.INFLUX.writeMeasurements(WritePrecision.NS, values));
        });
    }

    @com.influxdb.annotations.Measurement(name = "entities")
    public record Measurement(@Column(timestamp = true) Instant time,
                              @Column(tag = true) String worldName,
                              @Column double tickingEntities) {
    }
}
