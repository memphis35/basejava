package ru.javawebinar.basejava.exception;

public class NotExistException extends StorageException {

    public NotExistException(String uuid) {
        super("Resume " + uuid + " does not exist.", uuid);
    }
}
