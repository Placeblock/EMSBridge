package de.codelix.emsbridge.metrics.impl;

import com.influxdb.annotations.Column;
import de.codelix.emsbridge.metrics.AutoScheduledMetric;
import org.bukkit.Server;

import java.time.Instant;
import java.util.List;

public class OnlinePlayersMetric extends AutoScheduledMetric<OnlinePlayersMetric.Measurement> {
    private final Server server;

    public OnlinePlayersMetric(Server server) {
        super(1000);
        this.server = server;
    }

    @Override
    public List<Measurement> get() {
        final Instant time = Instant.now();
        return List.of(new Measurement(time, this.server.getOnlinePlayers().size()));
    }

    @com.influxdb.annotations.Measurement(name = "online_players")
    public record Measurement(@Column(timestamp = true) Instant time, @Column int online) {
    }
}
