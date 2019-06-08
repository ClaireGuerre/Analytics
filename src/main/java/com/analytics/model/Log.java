package com.analytics.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Objects;

public class Log {

    private LocalDateTime date;
    private Query query;

    public Log(LocalDateTime date, Query query) {
        this.date = date;
        this.query = query;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Query getQuery() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return Objects.equals(getDate(), log.getDate()) &&
                Objects.equals(getQuery(), log.getQuery());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getDate(), getQuery());
    }

    public boolean isBefore(Temporal endBound) {
        if (endBound instanceof LocalDate) {
            LocalDateTime endBoundLocalDateTime = ((LocalDate) endBound).atStartOfDay();
            return this.getDate().isBefore(endBoundLocalDateTime);
        }
        if (endBound instanceof LocalDateTime) {
            LocalDateTime endBoundLocalDateTime = (LocalDateTime)endBound;
            return this.getDate().isBefore(endBoundLocalDateTime);
        }
        return false;
    }

    public boolean isAfterOrEqual(Temporal startBound) {
        if (startBound instanceof LocalDate) {
            LocalDateTime startBoundLocalDateTime = ((LocalDate) startBound).atStartOfDay();
            return (this.getDate().isAfter(startBoundLocalDateTime) || this.getDate().isEqual(startBoundLocalDateTime));
        }
        if (startBound instanceof LocalDateTime) {
            LocalDateTime startBoundLocalDateTime = (LocalDateTime)startBound;
            return (this.getDate().isAfter(startBoundLocalDateTime) || this.getDate().isEqual(startBoundLocalDateTime));
        }
        return false;
    }
}
