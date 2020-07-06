package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SqlStorage implements Storage {

    private final SqlHelper helper = new SqlHelper(
            Config.get().getUrl(), Config.get().getUser(), Config.get().getPassword());

    @Override
    public void clear() {
        helper.prepare("DELETE FROM resume", ps -> {
            try {
                ps.execute();
            } catch (SQLException e) {
                throw new StorageException(e.getSQLState(), e);
            }
        });
    }

    @Override
    public void save(Resume resume) {
        helper.prepare("INSERT INTO resume VALUES(?, ?)", ps -> {
            try {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            } catch (SQLIntegrityConstraintViolationException e) {
                throw new ExistException("Already exist");
            } catch (SQLException e) {
                throw new StorageException(e.getMessage(), e);
            }
        });
    }

    @Override
    public void update(Resume resume) {
        helper.prepare("UPDATE resume SET full_name = ? WHERE uuid = ?", ps -> {
            try {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) throw new NotExistException("Not exist");
            } catch (SQLException e) {
                throw new StorageException(e.getMessage(), e);
            }
        });

    }

    @Override
    public Resume get(String uuid) {
        AtomicReference<String> fullName = new AtomicReference<>();
        helper.prepare("SELECT * FROM resume WHERE uuid = ?", ps -> {
            try {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) throw new NotExistException(uuid);
                fullName.set(rs.getString("full_name"));
            } catch (SQLException e) {
                throw new StorageException(e.getMessage(), e);
            }
        });
        return new Resume(uuid, fullName.get());
    }

    @Override
    public void delete(String uuid) {
        helper.prepare("DELETE FROM resume WHERE uuid = ?", ps -> {
            try {
                ps.setString(1, uuid);
                if (ps.executeUpdate() == 0) throw new NotExistException(uuid);
            } catch (SQLException e) {
                throw new StorageException(e.getMessage(), e);
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        helper.prepare("SELECT * FROM resume ORDER BY full_name", ps -> {
            try {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
                }
            } catch (SQLException e) {
                throw new StorageException(e.getMessage(), e);
            }
        });
        return resumes;
    }

    @Override
    public int size() {
        AtomicInteger size = new AtomicInteger();
        helper.prepare("SELECT COUNT(uuid) AS size FROM resume", ps -> {
            try {
                ResultSet rs = ps.executeQuery();
                rs.next();
                size.set(rs.getInt("size"));
            } catch (SQLException e) {
                throw new StorageException(e.getMessage(), e);
            }
        });
        return size.get();
    }
}