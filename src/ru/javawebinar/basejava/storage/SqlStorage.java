package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SqlStorage implements Storage {

    private final Logger log = Logger.getLogger(SqlStorage.class.getName());
    private final SqlHelper helper = new SqlHelper(
            Config.get().getUrl(), Config.get().getUser(), Config.get().getPassword());

    @Override
    public void clear() {
        helper.prepare("DELETE FROM resume", ps -> {
            ps.execute();
            log.info("Storage cleared");
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        helper.prepare("INSERT INTO resume VALUES(?, ?)", ps -> {
            try {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
                log.info("Resume successfully added");
                return null;
            } catch (SQLException e) {
                log.info("Resume " + resume.getUuid() + " already exist.");
                throw new ExistException(resume.getUuid());
            }
        });
    }

    @Override
    public void update(Resume resume) {
        helper.prepare("UPDATE resume SET full_name = ? WHERE uuid = ?", ps -> {
            ps.setString(1, resume.getFullName());
            ps.setString(2, resume.getUuid());
            if (ps.executeUpdate() == 0) {
                log.info("Resume doesn't exist");
                throw new NotExistException(resume.getUuid());
            }
            log.info("Resume successfully updated");
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return helper.prepare("SELECT * FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                log.info("Resume doesn't exist");
                throw new NotExistException(uuid);
            }
            log.info("Resume successfully receive");
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        helper.prepare("DELETE FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                log.info("Resume doesn't exist");
                throw new NotExistException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.prepare("SELECT * FROM resume ORDER BY full_name", ps -> {
            List<Resume> resumes = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
            log.info("List of resumes successfully received");
            return resumes;
        });
    }

    @Override
    public int size() {
        return helper.prepare("SELECT COUNT(uuid) AS size FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("size");
        });
    }
}