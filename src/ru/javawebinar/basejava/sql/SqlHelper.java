package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SqlHelper {
    private static final Logger LOGGER = Logger.getLogger(SqlHelper.class.getName());
    ConnectionFactory cf;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        cf = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public <T> T prepare(String query, Executor exec) {
        try (Connection connection = cf.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            LOGGER.info("Connection established.");
            return (T) exec.execute(ps);
        } catch (SQLException e) {
            LOGGER.warning("SQL Exception:" + e.getSQLState());
            throw new StorageException(e.getSQLState(), e);
        }
    }
}
