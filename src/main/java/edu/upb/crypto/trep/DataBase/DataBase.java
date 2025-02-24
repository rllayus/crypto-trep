package edu.upb.crypto.trep.DataBase;

import lombok.Getter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    @Getter
    private static DataBase instance = new DataBase();
    private static final String DATABASE_URL = "jdbc:sqlite:testing.db";
    private Connection connection;

    private DataBase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            connection.setAutoCommit(true);
            System.out.println("Connection to SQLite established.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}