package ru.javawebinar.basejava;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainFile {

    public static void main(String[] args) throws IOException {

        File dir = new File(".\\src\\ru\\javawebinar\\basejava");
        System.out.println(dir.isDirectory());
        File[] list = dir.listFiles();
        System.out.println(dir.getCanonicalPath());
        if (list != null) {
            for (File name : list) {
                showAllFiles(name);
            }
        }
    }

    public static void showAllFiles(File file) throws IOException {
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                showAllFiles(f);
            }
        }
        if (!file.isDirectory()) System.out.println(file.getCanonicalPath());
    }
}
