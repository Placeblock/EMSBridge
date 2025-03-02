package de.codelix.emsbridge.metrics;

import com.influxdb.client.domain.WritePrecision;
import de.codelix.emsbridge.EMSBridge;

import java.util.List;

public abstract class AutoScheduledMetric<M> extends ScheduledMetric {
    public AutoScheduledMetric(long period) {
        super(period);
    }
    public AutoScheduledMetric(long initDelay, long period) {
        super(initDelay, period);
    }

    protected abstract List<M> get();

    @Override
    public void run() {
        List<M> values = this.get();
        if (values.isEmpty()) return;
        EMSBridge.INFLUX.writeMeasurements(WritePrecision.NS, values);
    }
}
