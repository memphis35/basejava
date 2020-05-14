package ru.javawebinar.basejava.serializer;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.util.JSONParser;

import java.io.*;

public class JSONSerializer implements Serializer {

    @Override
    public void write(OutputStream out, Resume resume) throws IOException {
        try (BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(out))) {
            JSONParser.write(buffer, resume);
        }
    }

    @Override
    public Resume read(InputStream in) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(in))) {
            return JSONParser.read(buffer, Resume.class);
        }
    }
}
