package com.analytics.util;

import com.analytics.model.AnalyticsPeriod;
import com.analytics.model.Log;
import com.analytics.model.Query;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

    private final static Logger LOGGER = LogManager.getLogManager().getLogger(Parser.class.getName());
    private final static String SEPARATOR = "\t";

    private static Parser instance;

    private Parser() {

    }

    public static Parser getInstance() {
        if (instance == null) {
            instance = new Parser();
        }
        return instance;
    }

    public List<Log> parse(String inputFilePath) {
        try (Stream<String> stream = Files.lines(Paths.get(inputFilePath))) {

            List<Log> logs = stream.map(line -> parseLogLine(line)).collect(Collectors.toCollection(LinkedList::new));
            return logs;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return new LinkedList();
    }

    private Log parseLogLine(String line) {
        String[] columns = line.split(SEPARATOR);
        if (columns.length != 2) {
            LOGGER.log(Level.WARNING, "Invalid log line " + line);
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.parse(columns[0], DateParser.DATE_TIME_FORMATTER);
        Log log = new Log(dateTime, new Query(columns[1]));
        return log;
    }

}
