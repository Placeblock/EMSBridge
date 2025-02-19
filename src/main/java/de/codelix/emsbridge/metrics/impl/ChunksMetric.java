package de.codelix.emsbridge.metrics.impl;

import com.influxdb.annotations.Column;
import de.codelix.minecraftstatistics.metrics.AutoScheduledMetric;
import org.bukkit.Server;
import org.bukkit.World;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ChunksMetric extends AutoScheduledMetric<ChunksMetric.Measurement> {
    private final Server server;

    public ChunksMetric(Server server) {
        super(10000);
        this.server = server;
    }

    @Override
    public List<Measurement> get() {
        final Instant now = Instant.now();
        List<Measurement> worldChunks = new ArrayList<>();
        for (World world : this.server.getWorlds()) {
            worldChunks.add(new Measurement(now, world.getName(), world.getLoadedChunks().length));
        }
        return worldChunks;
    }

    @com.influxdb.annotations.Measurement(name = "loaded_chunks")
    public record Measurement(@Column(timestamp = true) Instant time,
                              @Column(tag = true) String worldName,
                              @Column double loadedChunks) {
    }
}
