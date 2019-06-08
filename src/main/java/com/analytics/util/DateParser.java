package com.analytics.util;

import com.analytics.model.AnalyticsPeriod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class DateParser {

    public static final DateTimeFormatter DATE_TIME_FORMATTER =DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private static Map<ChronoUnit, DateTimeFormatter> formatterMap = new HashMap<ChronoUnit, DateTimeFormatter>();

    static {
        formatterMap.put(ChronoUnit.SECONDS, DATE_TIME_FORMATTER);
        formatterMap.put(ChronoUnit.MINUTES, DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm", Locale.ENGLISH));
        formatterMap.put(ChronoUnit.HOURS, DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH", Locale.ENGLISH));
        formatterMap.put(ChronoUnit.DAYS, DateTimeFormatter.ofPattern(
                "yyyy-MM-dd", Locale.ENGLISH));
    }

    private static DateParser instance;

    private DateParser() {

    }

    public static DateParser getInstance() {
        if (instance == null) {
            instance = new DateParser();
        }
        return instance;
    }


    public AnalyticsPeriod parseDate(String dateStr) {
        ChronoUnit periodUnit = getPeriodUnit(dateStr);
        if (periodUnit == null) {
            throw new IllegalArgumentException("Wrong format of input date " + dateStr);
        }
        switch(periodUnit) {
            case YEARS:  {
                Integer year = Integer.valueOf(dateStr);
                LocalDate startBound = LocalDate.of(year,1,1);
                LocalDate endBound = startBound.plusMonths(12);
                return new AnalyticsPeriod(startBound, endBound);
            }
            case MONTHS:  {
                String[] splittedInput = dateStr.split("-");
                Integer year = Integer.valueOf(splittedInput[0]);
                Integer month = Integer.valueOf(splittedInput[1]);
                LocalDate startBound = LocalDate.of(year,month,1);
                LocalDate endBound = startBound.plusMonths(1);
                return new AnalyticsPeriod(startBound, endBound);
            }
            case DAYS:  {
                LocalDate startBound = LocalDate.parse(dateStr, formatterMap.get(periodUnit));
                LocalDate endBound = startBound.plus(1, periodUnit);
                return new AnalyticsPeriod(startBound, endBound);
            }
            default: {
                LocalDateTime startBound = LocalDateTime.parse(dateStr, formatterMap.get(periodUnit));
                LocalDateTime endBound = startBound.plus(1, periodUnit);
                return new AnalyticsPeriod(startBound, endBound);
            }
        }
    }

    private ChronoUnit getPeriodUnit(String dateStr) {
        if (Pattern.matches("[0-9]{4}", dateStr)) {
            return ChronoUnit.YEARS;
        }
        if (Pattern.matches("[0-9]{4}-[0-9]{2}", dateStr)) {
            return ChronoUnit.MONTHS;
        }
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", dateStr)) {
            return ChronoUnit.DAYS;
        }
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}", dateStr)) {
            return ChronoUnit.HOURS;
        }
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}", dateStr)) {
            return ChronoUnit.MINUTES;
        }
        if (Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}", dateStr)) {
            return ChronoUnit.SECONDS;
        }
        return null;
    }
}
