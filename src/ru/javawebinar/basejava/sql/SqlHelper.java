package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlHelper {

    ConnectionFactory cf;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        cf = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public void prepare(String query, Executor exec) {
        try (Connection connection = cf.getConnection()) {
            System.out.println("Connection established.");
            exec.execute(connection.prepareStatement(query));
        } catch (SQLException e) {
            throw new StorageException(e.getSQLState(), e);
        }
    }
}
