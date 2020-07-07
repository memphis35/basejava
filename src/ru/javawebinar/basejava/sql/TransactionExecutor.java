package ru.javawebinar.basejava.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionExecutor<T> {

    T transactionExecute(Connection conn) throws SQLException;
}
