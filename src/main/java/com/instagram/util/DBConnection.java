package com.instagram.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final Logger logger =
            LoggerFactory.getLogger(DBConnection.class);

    private static final String URL =
            "jdbc:mysql://localhost:3306/socialmediaapp";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "Manasa@1604";

    public static Connection getConnection() throws SQLException {

        try {
            Connection connection =
                    DriverManager.getConnection(URL, USERNAME, PASSWORD);

            logger.info("Database connection established successfully");

            return connection;

        } catch (SQLException e) {

            logger.error("Failed to establish database connection", e);

            throw e;
        }
    }
}