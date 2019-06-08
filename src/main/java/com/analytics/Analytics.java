package com.analytics;

import com.analytics.model.AnalyticsPeriod;
import com.analytics.model.Log;
import com.analytics.model.Query;
import com.analytics.util.DateParser;
import com.analytics.util.Parser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Path("/1/queries")
@Produces(MediaType.APPLICATION_JSON)
public class Analytics {
    private final static Logger LOGGER = LogManager.getLogManager().getLogger(Analytics.class.getName());
    private String inputFilePath;
    List<Log> logs;
    
    public Analytics() {
        init();
    }

    private void init() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("analytics.ini"));
            if (lines.size()>0) {
                String line = lines.get(0);
                String[] splittedLine = line.split("=");
                if (splittedLine.length == 2) {
                    inputFilePath = splittedLine[1];
                } else {
                    throw new IllegalArgumentException("Wrong format of ini file");
                }
            } else {
                throw new IllegalArgumentException("No path specified in ini file");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @GET
    @Path("count/{date}")
    public long getNbDistinctQueries(@PathParam("date") String dateStr) {
        if (logs == null) {
            logs = Parser.getInstance().parse(inputFilePath);
        }
        AnalyticsPeriod period = DateParser.getInstance().parseDate(dateStr);
        Temporal endBound = period.getEndBound();
        Temporal startBound = period.getStartBound();
        long count = logs.stream().filter(log -> log.isBefore(endBound) && log.isAfterOrEqual(startBound))
                .map(log -> log.getQuery()).distinct().count();
        return count;
    }

    @GET
    @Path("popular/{date}/{size}")
    public List<Query> getTopPopularQueries(@PathParam("date") String dateStr, @PathParam("size") int size) {
        if (logs == null) {
            logs = Parser.getInstance().parse(inputFilePath);
        }
        AnalyticsPeriod period = DateParser.getInstance().parseDate(dateStr);
        Temporal endBound = period.getEndBound();
        Temporal startBound = period.getStartBound();
        Map<Query, List<LocalDateTime>> mapQueryToListDates = logs.stream().
                filter(log -> log.isBefore(endBound) && log.isAfterOrEqual(startBound))
                .collect(groupingBy(log -> log.getQuery(), mapping(log -> log.getDate(), toList())));

        List<Query> queries = mapQueryToListDates.entrySet()
                .stream()
                .sorted((c1, c2) -> ((Integer)c2.getValue().size()).compareTo(c1.getValue().size())).
                limit(size).map(entry -> entry.getKey()).collect(toList());


        return queries;

    }
}
