package ru.javawebinar.basejava.exception;

public class StorageException extends RuntimeException {
    private String uuid;

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, Exception cause) {
        super(message, cause);
    }

    public StorageException(String message, Exception cause, String uuid) {
        super(message, cause);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
