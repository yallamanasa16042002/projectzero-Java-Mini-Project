package com.instagram.dao;

import com.instagram.exception.DataAccessException;
import com.instagram.model.User;
import com.instagram.query.UserQueries;
import com.instagram.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDAOImpl.class);

    // --------------------------------------------------
    // CREATE USER
    // --------------------------------------------------

    @Override
    public int createUser(User user) {

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             UserQueries.INSERT_USER,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setString(
                    1,
                    user.getUsername()
            );

            statement.setString(
                    2,
                    user.getEmail()
            );

            statement.setString(
                    3,
                    user.getPasswordHash()
            );

            statement.setString(
                    4,
                    user.getStatus()
            );

            statement.setString(
                    5,
                    user.getRole()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    int userId =
                            resultSet.getInt(1);

                    logger.info(
                            "User created successfully with ID: {}",
                            userId
                    );

                    return userId;
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {

            logger.warn(
                    "User creation failed because username or email already exists"
            );

            throw new DataAccessException(
                    "Username or email already exists",
                    e
            );

        } catch (SQLException e) {

            logger.error(
                    "Database error while creating user",
                    e
            );

            throw new DataAccessException(
                    "Error while creating user",
                    e
            );
        }

        return 0;
    }

    // --------------------------------------------------
    // FIND USER BY USERNAME
    // --------------------------------------------------

    @Override
    public User findByUsername(
            String username) {

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             UserQueries.FIND_USER_BY_USERNAME
                     )) {

            statement.setString(
                    1,
                    username
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info(
                            "User found by username: {}",
                            username
                    );

                    return mapUser(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Database error while finding user by username: {}",
                    username,
                    e
            );

            throw new DataAccessException(
                    "Error while finding user by username",
                    e
            );
        }

        logger.info(
                "No user found for username: {}",
                username
        );

        return null;
    }

    // --------------------------------------------------
    // FIND USER BY EMAIL
    // --------------------------------------------------

    @Override
    public User findByEmail(
            String email) {

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             UserQueries.FIND_USER_BY_EMAIL
                     )) {

            statement.setString(
                    1,
                    email
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info(
                            "User found by email: {}",
                            email
                    );

                    return mapUser(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Database error while finding user by email: {}",
                    email,
                    e
            );

            throw new DataAccessException(
                    "Error while finding user by email",
                    e
            );
        }

        logger.info(
                "No user found for email: {}",
                email
        );

        return null;
    }

    // --------------------------------------------------
    // FIND ACTIVE USER FOR LOGIN
    // --------------------------------------------------

    @Override
    public User findActiveUserForLogin(
            String identifier) {

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             UserQueries.FIND_ACTIVE_USER_FOR_LOGIN
                     )) {

            statement.setString(
                    1,
                    identifier
            );

            statement.setString(
                    2,
                    identifier
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info(
                            "Active user found for login identifier: {}",
                            identifier
                    );

                    return mapUser(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Database error during login lookup for identifier: {}",
                    identifier,
                    e
            );

            throw new DataAccessException(
                    "Error while finding active user for login",
                    e
            );
        }

        logger.info(
                "No active user found for login identifier: {}",
                identifier
        );

        return null;
    }

    // --------------------------------------------------
    // UPDATE USER
    // --------------------------------------------------

    @Override
    public boolean updateUser(
            User user) {

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             UserQueries.UPDATE_USER
                     )) {

            statement.setString(
                    1,
                    user.getUsername()
            );

            statement.setString(
                    2,
                    user.getEmail()
            );

            statement.setInt(
                    3,
                    user.getUserId()
            );

            int rows =
                    statement.executeUpdate();

            if (rows == 1) {

                logger.info(
                        "User updated successfully with ID: {}",
                        user.getUserId()
                );

                return true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {

            logger.warn(
                    "User update failed because username or email already exists"
            );

            throw new DataAccessException(
                    "Username or email already exists",
                    e
            );

        } catch (SQLException e) {

            logger.error(
                    "Database error while updating user ID: {}",
                    user.getUserId(),
                    e
            );

            throw new DataAccessException(
                    "Error while updating user",
                    e
            );
        }

        logger.warn(
                "No user updated for ID: {}",
                user.getUserId()
        );

        return false;
    }

    // --------------------------------------------------
    // CHANGE PASSWORD
    // --------------------------------------------------

    @Override
    public boolean changePassword(
            int userId,
            String newPassword) {

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             UserQueries.CHANGE_PASSWORD
                     )) {

            statement.setString(
                    1,
                    newPassword
            );

            statement.setInt(
                    2,
                    userId
            );

            int rows =
                    statement.executeUpdate();

            if (rows == 1) {

                logger.info(
                        "Password changed successfully for user ID: {}",
                        userId
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Database error while changing password for user ID: {}",
                    userId,
                    e
            );

            throw new DataAccessException(
                    "Error while changing password",
                    e
            );
        }

        logger.warn(
                "Password was not changed for user ID: {}",
                userId
        );

        return false;
    }

    // --------------------------------------------------
    // SEARCH USERS
    // --------------------------------------------------

    @Override
    public List<User> searchUsersByUsername(
            String username) {

        List<User> users =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             UserQueries.SEARCH_USERS_BY_USERNAME
                     )) {

            statement.setString(
                    1,
                    "%" + username + "%"
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    users.add(
                            mapUser(resultSet)
                    );
                }
            }

            logger.info(
                    "User search completed for '{}'. Results found: {}",
                    username,
                    users.size()
            );

        } catch (SQLException e) {

            logger.error(
                    "Database error while searching users by username: {}",
                    username,
                    e
            );

            throw new DataAccessException(
                    "Error while searching users",
                    e
            );
        }

        return users;
    }

    // --------------------------------------------------
    // MAP RESULT SET TO USER
    // --------------------------------------------------

    private User mapUser(
            ResultSet resultSet)
            throws SQLException {

        Timestamp createdAt =
                resultSet.getTimestamp(
                        "created_at"
                );

        Timestamp updatedAt =
                resultSet.getTimestamp(
                        "updated_at"
                );

        return new User(
                resultSet.getInt(
                        "user_id"
                ),
                resultSet.getString(
                        "username"
                ),
                resultSet.getString(
                        "email"
                ),
                resultSet.getString(
                        "password_hash"
                ),
                resultSet.getString(
                        "status"
                ),
                resultSet.getString(
                        "role"
                ),
                createdAt != null
                        ? createdAt.toLocalDateTime()
                        : null,
                updatedAt != null
                        ? updatedAt.toLocalDateTime()
                        : null
        );
    }
}