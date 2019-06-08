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

    public static final DateTimeFormatter DATE_TIME_FORMATTER =DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private static Map<PeriodUnit, DateTimeFormatter> formatterMap = new HashMap<PeriodUnit, DateTimeFormatter>();

    private final static Logger LOGGER = LogManager.getLogManager().getLogger(Parser.class.getName());
    private final static String SEPARATOR = " ";

    static {
        formatterMap.put(PeriodUnit.SECOND, DATE_TIME_FORMATTER);
        formatterMap.put(PeriodUnit.MINUTE, DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm", Locale.ENGLISH));
        formatterMap.put(PeriodUnit.HOUR, DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH", Locale.ENGLISH));
        formatterMap.put(PeriodUnit.DAY, DateTimeFormatter.ofPattern(
                "yyyy-MM-dd", Locale.ENGLISH));
    }

    private enum PeriodUnit {YEAR, MONTH, DAY, HOUR, MINUTE, SECOND};

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
        LocalDateTime dateTime = LocalDateTime.parse(columns[0], DATE_TIME_FORMATTER);
        Log log = new Log(dateTime, new Query(columns[1]));
        return log;
    }

    public AnalyticsPeriod parseDate(String dateStr) {
        PeriodUnit periodUnit = getPeriodUnit(dateStr);
        if (periodUnit == null) {
            throw new IllegalArgumentException("Wrong format of input date " + dateStr);
        }
        switch(periodUnit) {
            case YEAR:  {
                Integer year = Integer.valueOf(dateStr);
                LocalDate startBound = LocalDate.of(year,1,1);
                LocalDate endBound = startBound.plusYears(1);
                return new AnalyticsPeriod(startBound, endBound);
            }
            case MONTH:  {
                String[] splittedInput = dateStr.split("-");
                Integer year = Integer.valueOf(splittedInput[0]);
                Integer month = Integer.valueOf(splittedInput[1]);
                LocalDate startBound = LocalDate.of(year,month,1);
                LocalDate endBound = startBound.plusMonths(1);
                return new AnalyticsPeriod(startBound, endBound);
            }
            case DAY:  {
                LocalDate startBound = LocalDate.parse(dateStr, formatterMap.get(PeriodUnit.DAY));
                LocalDate endBound = startBound.plusDays(1);
                return new AnalyticsPeriod(startBound, endBound);
            }
            case HOUR:  {
                LocalDateTime startBound = LocalDateTime.parse(dateStr, formatterMap.get(PeriodUnit.HOUR));
                LocalDateTime endBound = startBound.plus(1, ChronoUnit.HOURS);
                return new AnalyticsPeriod(startBound, endBound);
            }
            case MINUTE:  {
                LocalDateTime startBound = LocalDateTime.parse(dateStr, formatterMap.get(PeriodUnit.MINUTE));
                LocalDateTime endBound = startBound.plus(1, ChronoUnit.MINUTES);
                return new AnalyticsPeriod(startBound, endBound);
            }
            case SECOND:  {
                LocalDateTime startBound = LocalDateTime.parse(dateStr, formatterMap.get(PeriodUnit.SECOND));
                LocalDateTime endBound = startBound.plus(1, ChronoUnit.SECONDS);
                return new AnalyticsPeriod(startBound, endBound);
            }
        }
        return null;
    }

    private PeriodUnit getPeriodUnit(String dateStr) {
        if (Pattern.matches("[0-9]{4}", dateStr)) {
            return PeriodUnit.YEAR;
        }
        if (Pattern.matches("[0-9]{4}-[0-9]{2}", dateStr)) {
            return PeriodUnit.MONTH;
        }
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", dateStr)) {
            return PeriodUnit.DAY;
        }
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}", dateStr)) {
            return PeriodUnit.HOUR;
        }
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}", dateStr)) {
            return PeriodUnit.MINUTE;
        }
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}", dateStr)) {
            return PeriodUnit.SECOND;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println((new Parser()).parseDate("2015"));
        System.out.println((new Parser()).parseDate("2015-04"));
        System.out.println((new Parser()).parseDate("2015-04-23"));
        System.out.println((new Parser()).parseDate("2015-04-23 00"));
        System.out.println((new Parser()).parseDate("2015-04-23 00:25"));
        System.out.println((new Parser()).parseDate("2015-04-23 00:25:15"));
    }
}
