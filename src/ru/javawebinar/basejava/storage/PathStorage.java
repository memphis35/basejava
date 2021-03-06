package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.serializer.Serializer;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private Path dir;
    private Serializer strategy;

    PathStorage(String dir, Serializer serializer) {
        Objects.requireNonNull(dir, "directory must not be null");
        Objects.requireNonNull(serializer, "Serialization method must not be null");
        this.dir = Paths.get(dir);
        this.strategy = serializer;
        if (!Files.isDirectory(this.dir) || !Files.isWritable(this.dir)) {
            throw new IllegalArgumentException(dir + " isn't directory or can't be writeable.");
        }
    }

    @Override
    protected void saveToStorage(Path searchKey, Resume resume) {
        try {
            Files.createFile(searchKey);
            updateToStorage(searchKey, resume);
        } catch (IOException e) {
            throw new StorageException("Can't create file.", e, searchKey.toString());
        }
    }

    @Override
    protected void updateToStorage(Path searchKey, Resume resume) {
        try {
            strategy.write(new BufferedOutputStream(Files.newOutputStream(searchKey)), resume);
        } catch (IOException e) {
            throw new StorageException("Storage write error.", e, searchKey.getFileName().toString());
        }
    }

    @Override
    protected Resume getResume(Path searchKey) {
        try {
            return strategy.read(new BufferedInputStream(Files.newInputStream(searchKey)));
        } catch (IOException e) {
            throw new StorageException("Storage read error", e, searchKey.getFileName().toString());
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
        return dir.resolve(uuid);
    }

    @Override
    public void clear() {
        checkFileList().forEach(this::deleteFromStorage);
    }

    @Override
    public int size() {
        return (int) checkFileList().count();
    }

    @Override
    public List<Resume> getAll() {
        return checkFileList().map(this::getResume).collect(Collectors.toList());
    }

    private Stream<Path> checkFileList() {
        try {
            return Files.list(dir);
        } catch (IOException e) {
            throw new StorageException("Directory read error", e, null);
        }
    }

    public void setStrategy(Serializer strategy) {
        this.strategy = strategy;
    }
}
