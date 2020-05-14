package ru.javawebinar.basejava.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.from(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).parse(v));
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return v.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
    }
}
