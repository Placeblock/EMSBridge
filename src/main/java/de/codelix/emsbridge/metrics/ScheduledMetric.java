package de.codelix.emsbridge.metrics;

import de.codelix.emsbridge.EMSBridge;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class ScheduledMetric extends Metric implements Runnable {
    protected final long initDelay;
    protected final long period;
    private ScheduledFuture<?> future;

    public ScheduledMetric(long initDelay, long period) {
        this.initDelay = initDelay;
        this.period = period;
    }
    public ScheduledMetric(long period) {
        this.initDelay = 0;
        this.period = period;
    }

    @Override
    public void enable() {
        this.future = EMSBridge.SCHEDULER.scheduleAtFixedRate(this, this.initDelay, this.period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void disable() {
        if (this.future == null) return;
        this.future.cancel(true);
    }

}
