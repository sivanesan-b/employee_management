package com.employeemanagement.config;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConfiguration {
    private static final Logger LOGGER = LoggerConfig.getLogger();
    private static final String URL = "jdbc:postgresql://localhost:5432/EmployeeManagement";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "6682201";
    private static Connection connection = null;
    private DbConfiguration(){ }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE,"Unable to connect to DB", e);
            }
        }
        return connection;
    }
}