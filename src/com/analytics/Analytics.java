package com.analytics;

import com.analytics.model.AnalyticsPeriod;
import com.analytics.model.Log;
import com.analytics.model.Query;
import com.analytics.util.Parser;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class Analytics {

    private static final String INPUT_FILE_PATH = "/home/claire/Téléchargements/hn_logs.tsv";
    List<Log> logs;

    public long getNbDistinctQueries(String dateStr) {
        if (logs == null) {
            logs = Parser.getInstance().parse(INPUT_FILE_PATH);
        }
        AnalyticsPeriod period = Parser.getInstance().parseDate(dateStr);
        Temporal endBound = period.getEndBound();
        Temporal startBound = period.getStartBound();
        long count = logs.stream().filter(log -> log.isBefore(endBound) && log.isAfter(startBound))
                .map(log -> log.getQuery()).distinct().count();
        return count;
    }

    public List<Query> getTopPopularQueries(String dateStr, int size) {
        if (logs == null) {
            logs = Parser.getInstance().parse(INPUT_FILE_PATH);
        }
        AnalyticsPeriod period = Parser.getInstance().parseDate(dateStr);
        Temporal endBound = period.getEndBound();
        Temporal startBound = period.getStartBound();
        Map<Query, List<LocalDateTime>> mapQueryToListDates = logs.stream().
                filter(log -> log.isBefore(endBound) && log.isAfter(startBound))
                .collect(groupingBy(log -> log.getQuery(), mapping(log -> log.getDate(), toList())));

        List<Query> queries = mapQueryToListDates.entrySet()
                .stream()
                .sorted((c1, c2) -> ((Integer)c1.getValue().size()).compareTo(c2.getValue().size())).
                limit(size).map(entry -> entry.getKey()).collect(toList());


        return queries;

    }
}
