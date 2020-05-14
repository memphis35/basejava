package ru.javawebinar.basejava.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.javawebinar.basejava.model.Section;

import java.io.Reader;
import java.io.Writer;

public class JSONParser {

    private static final Gson PARSER = new GsonBuilder()
            .registerTypeAdapter(Section.class, new SectionClassAdapter<>())
            .create();

    public static <T> T read(Reader reader, Class<T> clazz) {
        return (T) PARSER.fromJson(reader, clazz);
    }

    public static <T> void write(Writer writer, T object) {
        PARSER.toJson(object, writer);
    }
}
