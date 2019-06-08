package com.analytics.model;

import java.time.temporal.Temporal;

public class AnalyticsPeriod {

    private Temporal endBound;
    private Temporal startBound;

    public AnalyticsPeriod(Temporal startBound, Temporal endBound) {
        this.endBound = endBound;
        this.startBound = startBound;
    }

    public Temporal getEndBound() {
        return endBound;
    }

    public Temporal getStartBound() {
        return startBound;
    }

    @Override
    public String toString() {
        return "AnalyticsPeriod{" +
                "endBound=" + endBound +
                ", startBound=" + startBound +
                '}';
    }
}
