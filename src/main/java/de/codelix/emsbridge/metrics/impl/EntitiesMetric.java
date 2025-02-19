package de.codelix.emsbridge.metrics.impl;

import com.influxdb.annotations.Column;
import de.codelix.emsbridge.metrics.AutoScheduledMetric;
import org.bukkit.Server;
import org.bukkit.World;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EntitiesMetric extends AutoScheduledMetric<EntitiesMetric.Measurement> {
    private final Server server;

    public EntitiesMetric(Server server) {
        super(10000);
        this.server = server;
    }

    @Override
    public List<Measurement> get() {
        final Instant now = Instant.now();
        List<Measurement> worldEntities = new ArrayList<>();
        for (World world : this.server.getWorlds()) {
            worldEntities.add(new Measurement(now, world.getName(), world.getEntityCount()));
        }
        return worldEntities;
    }

    @com.influxdb.annotations.Measurement(name = "entities")
    public record Measurement(@Column(timestamp = true) Instant time,
                              @Column(tag = true) String worldName,
                              @Column double tickingEntities) {
    }
}
