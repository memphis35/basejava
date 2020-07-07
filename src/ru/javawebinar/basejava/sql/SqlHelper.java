package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SqlHelper {
    private final Logger log = Logger.getLogger(SqlHelper.class.getName());
    private final ConnectionFactory cf;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        cf = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public <T> T prepare(String query, Executor<T> exec) {
        try (Connection connection = cf.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            log.info("Connection established.");
            return exec.execute(ps);
        } catch (SQLException e) {
            log.warning("SQL Exception:" + e.getSQLState());
            throw new StorageException(e.getSQLState(), e);
        }
    }
}
