package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private Path dir;

    public AbstractPathStorage(String dir) {
        Objects.requireNonNull(dir, "directory must not be null");
        this.dir = Paths.get(dir);
        if (!Files.isDirectory(this.dir) || !Files.isWritable(this.dir)) {
            throw new IllegalArgumentException(dir  + " isn't directory or can't be writeable.");
        }
    }

    @Override
    public List<Resume> getAll() {
        List<Resume> result = new ArrayList<>();
        try {
            Files.list(dir).forEach(file -> result.add(getResume(file)));
        } catch (IOException e) {
            throw new StorageException("Storage read error", e, null);
        }
        return result;
    }

    @Override
    protected void saveToStorage(Path searchKey, Resume resume) {
        try {
            Files.createFile(searchKey);
            updateToStorage(searchKey, resume);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateToStorage(Path searchKey, Resume resume) {
        try {
            write(new FileOutputStream(searchKey.toFile()), resume);
        } catch (FileNotFoundException e) {
            throw new StorageException("File not found", e, searchKey.getFileName().toString());
        }
    }

    @Override
    protected Resume getResume(Path searchKey) {
        try {
            return read(new FileInputStream(searchKey.toFile()));
        } catch (FileNotFoundException e) {
            throw new StorageException("File not found", e, searchKey.getFileName().toString());
        }
    }

    @Override
    protected boolean isExistKey(Path searchKey) {
        return Files.exists(searchKey);
    }

    @Override
    protected void deleteFromStorage(Path searchKey) {
        try {
            Files.delete(searchKey);
        } catch (IOException e) {
            throw new StorageException("Delete error.", e, searchKey.getFileName().toString());
        }
    }

    @Override
    public Path getSearchKey(String uuid) {
        return Paths.get(String.valueOf(dir), uuid);
    }

    @Override
    public void clear() {
        try {
            Files.list(dir).forEach(this::deleteFromStorage);
        } catch (IOException e) {
            throw new StorageException("Directory read error", e, null);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(dir).count();
        } catch (IOException e) {
            throw new StorageException("Directory read error", e, null);
        }
    }

    abstract void write(FileOutputStream fileOut, Resume resume);

    abstract Resume read(FileInputStream fileIn);
}
