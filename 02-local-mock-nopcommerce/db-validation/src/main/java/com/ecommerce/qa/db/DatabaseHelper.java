package com.ecommerce.qa.db;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseHelper {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            try (InputStream input = DatabaseHelper.class.getClassLoader().getResourceAsStream("config/database.properties")) {
                props.load(input);
                String url = props.getProperty("database.docker.url");
                String user = props.getProperty("database.docker.username");
                String password = props.getProperty("database.docker.password");
                connection = DriverManager.getConnection(url, user, password);
            } catch (Exception e) {
                throw new SQLException("Cannot connect to database", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}