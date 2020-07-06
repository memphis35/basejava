package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Executor<TYPE> {

    TYPE execute(PreparedStatement ps) throws SQLException;
}
