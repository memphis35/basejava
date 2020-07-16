package ru.javawebinar.basejava;

import java.io.*;
import java.util.Properties;

public class Config {

    private static final File PROPS = new File("config/resumes.properties");
    private static final Config INSTANCE = new Config();
    private final File storageDir;
    private final String url;
    private final String user;
    private final String password;

    private Config() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try(InputStream is = loader.getResourceAsStream(PROPS.getPath())) {
            Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS);
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public File getStorageDir() {
        return storageDir;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
