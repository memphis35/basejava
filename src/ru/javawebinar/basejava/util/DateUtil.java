package ru.javawebinar.basejava.util;

import java.time.*;

public class DateUtil {

    public static LocalDate of(DayOfWeek day, Month month, Year year) {
        return LocalDate.of(year.getValue(), month.getValue(), day.getValue());
    }
}
