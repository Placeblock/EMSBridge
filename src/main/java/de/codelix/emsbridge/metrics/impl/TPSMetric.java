package de.codelix.emsbridge.metrics.impl;

import com.influxdb.annotations.Column;
import de.codelix.emsbridge.metrics.AutoScheduledMetric;
import org.bukkit.Server;

import java.time.Instant;
import java.util.List;

public class TPSMetric extends AutoScheduledMetric<TPSMetric.Measurement> {
    private final Server server;

    public TPSMetric(Server server) {
        super(1000);
        this.server = server;
    }

    @Override
    public List<Measurement> get() {
        final Instant time = Instant.now();
        final double tps = this.server.getTPS()[0];
        return List.of(new Measurement(time, tps));
    }

    @com.influxdb.annotations.Measurement(name = "tps")
    public record Measurement(
            @Column(timestamp = true) Instant time,
            @Column double tps
    ) {}
}
