package ru.javawebinar.basejava.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.time.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class DateUtil {

    public static LocalDate of(int day, int month, int year) {
        return LocalDate.of(year, month, day);
    }
}
