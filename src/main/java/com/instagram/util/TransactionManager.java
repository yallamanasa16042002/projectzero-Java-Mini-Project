package com.instagram.util;

import com.instagram.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public final class TransactionManager {

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionManager.class);

    private TransactionManager() {
    }

    @FunctionalInterface
    public interface TransactionOperation<T> {

        T execute(Connection connection) throws SQLException;
    }

    public static <T> T execute(TransactionOperation<T> operation) {

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();

            connection.setAutoCommit(false);

            T result = operation.execute(connection);

            connection.commit();

            logger.info("Transaction committed successfully");

            return result;

        } catch (SQLException e) {

            rollback(connection);

            logger.error(
                    "Transaction failed and was rolled back",
                    e
            );

            throw new DataAccessException(
                    "Transaction failed",
                    e
            );

        } catch (RuntimeException e) {

            rollback(connection);

            logger.error(
                    "Transaction failed and was rolled back",
                    e
            );

            throw e;

        } finally {

            closeConnection(connection);
        }
    }

    private static void rollback(Connection connection) {

        if (connection != null) {

            try {
                connection.rollback();

                logger.warn(
                        "Transaction rolled back successfully"
                );

            } catch (SQLException e) {

                logger.error(
                        "Failed to rollback transaction",
                        e
                );
            }
        }
    }

    private static void closeConnection(Connection connection) {

        if (connection != null) {

            try {
                connection.close();

            } catch (SQLException e) {

                logger.error(
                        "Failed to close transaction connection",
                        e
                );
            }
        }
    }
}