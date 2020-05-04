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
        if (list != null) showAllFiles(dir, "");
    }

    private static void showAllFiles(File file, String tab) {
        if (file.isDirectory()) {
            tab = tab + "\t";
            System.out.println(tab + "Folder: " + file.getName());
            for (File f : Objects.requireNonNull(file.listFiles())) {
                showAllFiles(f, tab);
            }
        }
        if (!file.isDirectory()) System.out.println(tab + "\tFile: " + file.getName());
    }
}
