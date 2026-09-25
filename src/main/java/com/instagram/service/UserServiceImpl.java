package com.instagram.service;

import com.instagram.dao.UserDAO;
import com.instagram.dao.UserDAOImpl;
import com.instagram.exception.ValidationException;
import com.instagram.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private static final int MAX_USERNAME_LENGTH = 50;
    private static final int MAX_EMAIL_LENGTH = 100;

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    // --------------------------------------------------
    // REGISTER USER
    // --------------------------------------------------

    @Override
    public int registerUser(User user) {

        if (user == null) {

            logger.warn(
                    "Registration failed: user details are null"
            );

            throw new ValidationException(
                    "User details cannot be null"
            );
        }

        validateUsername(
                user.getUsername()
        );

        validateEmail(
                user.getEmail()
        );

        validatePassword(
                user.getPasswordHash()
        );

        if (user.getStatus() == null ||
                user.getStatus().isBlank()) {

            user.setStatus(
                    "ACTIVE"
            );
        }

        if (user.getRole() == null ||
                user.getRole().isBlank()) {

            user.setRole(
                    "USER"
            );
        }

        int userId =
                userDAO.createUser(user);

        if (userId <= 0) {

            logger.warn(
                    "User registration failed for username: {}",
                    user.getUsername()
            );

            throw new ValidationException(
                    "User registration failed"
            );
        }

        logger.info(
                "User registration successful with ID: {}",
                userId
        );

        return userId;
    }

    // --------------------------------------------------
    // LOGIN USER
    // --------------------------------------------------

    @Override
    public User loginUser(
            String identifier,
            String password) {

        if (identifier == null ||
                identifier.isBlank()) {

            logger.warn(
                    "Login failed: username/email is empty"
            );

            throw new ValidationException(
                    "Username or email cannot be empty"
            );
        }

        if (password == null ||
                password.isBlank()) {

            logger.warn(
                    "Login failed: password is empty"
            );

            throw new ValidationException(
                    "Password cannot be empty"
            );
        }

        User user =
                userDAO.findActiveUserForLogin(
                        identifier.trim()
                );

        if (user == null) {

            logger.warn(
                    "Login failed: active user not found for: {}",
                    identifier.trim()
            );

            throw new ValidationException(
                    "Invalid username/email or account is inactive"
            );
        }

        if (!password.equals(
                user.getPasswordHash())) {

            logger.warn(
                    "Login failed: invalid password for: {}",
                    identifier.trim()
            );

            throw new ValidationException(
                    "Invalid username/email or password"
            );
        }

        logger.info(
                "User login successful for: {}",
                user.getUsername()
        );

        return user;
    }

    // --------------------------------------------------
    // FIND USER BY USERNAME
    // --------------------------------------------------

    @Override
    public User findUserByUsername(
            String username) {

        validateUsername(
                username
        );

        return userDAO.findByUsername(
                username.trim()
        );
    }

    // --------------------------------------------------
    // FIND USER BY EMAIL
    // --------------------------------------------------

    @Override
    public User findUserByEmail(
            String email) {

        validateEmail(
                email
        );

        return userDAO.findByEmail(
                email.trim()
        );
    }

    // --------------------------------------------------
    // UPDATE USER
    // --------------------------------------------------

    @Override
    public boolean updateUser(
            User user) {

        if (user == null) {

            logger.warn(
                    "User update failed: user details are null"
            );

            throw new ValidationException(
                    "User details cannot be null"
            );
        }

        if (user.getUserId() <= 0) {

            logger.warn(
                    "User update failed: invalid user ID"
            );

            throw new ValidationException(
                    "Invalid user ID"
            );
        }

        validateUsername(
                user.getUsername()
        );

        validateEmail(
                user.getEmail()
        );

        boolean updated =
                userDAO.updateUser(user);

        if (!updated) {

            logger.warn(
                    "User update failed with ID: {}",
                    user.getUserId()
            );

            throw new ValidationException(
                    "User update failed"
            );
        }

        logger.info(
                "User updated successfully with ID: {}",
                user.getUserId()
        );

        return true;
    }

    // --------------------------------------------------
    // CHANGE PASSWORD
    // --------------------------------------------------

    @Override
    public boolean changePassword(
            int userId,
            String newPassword) {

        if (userId <= 0) {

            logger.warn(
                    "Password change failed: invalid user ID"
            );

            throw new ValidationException(
                    "Invalid user ID"
            );
        }

        validatePassword(
                newPassword
        );

        boolean changed =
                userDAO.changePassword(
                        userId,
                        newPassword
                );

        if (!changed) {

            logger.warn(
                    "Password change failed for user ID: {}",
                    userId
            );

            throw new ValidationException(
                    "Password change failed"
            );
        }

        logger.info(
                "Password changed successfully for user ID: {}",
                userId
        );

        return true;
    }

    // --------------------------------------------------
    // SEARCH USERS
    // --------------------------------------------------

    @Override
    public List<User> searchUsersByUsername(
            String username) {

        validateUsername(
                username
        );

        List<User> users =
                userDAO.searchUsersByUsername(
                        username.trim()
                );

        logger.info(
                "User search completed for '{}'. Results: {}",
                username.trim(),
                users.size()
        );

        return users;
    }

    // --------------------------------------------------
    // USERNAME VALIDATION
    // --------------------------------------------------

    private void validateUsername(
            String username) {

        if (username == null ||
                username.isBlank()) {

            throw new ValidationException(
                    "Username cannot be empty"
            );
        }

        if (username.trim().length()
                > MAX_USERNAME_LENGTH) {

            throw new ValidationException(
                    "Username cannot exceed "
                            + MAX_USERNAME_LENGTH
                            + " characters"
            );
        }
    }

    // --------------------------------------------------
    // EMAIL VALIDATION
    // --------------------------------------------------

    private void validateEmail(
            String email) {

        if (email == null ||
                email.isBlank()) {

            throw new ValidationException(
                    "Email cannot be empty"
            );
        }

        if (email.trim().length()
                > MAX_EMAIL_LENGTH) {

            throw new ValidationException(
                    "Email cannot exceed "
                            + MAX_EMAIL_LENGTH
                            + " characters"
            );
        }

        if (!isValidEmail(
                email.trim()
        )) {

            throw new ValidationException(
                    "Invalid email format"
            );
        }
    }

    // --------------------------------------------------
    // PASSWORD VALIDATION
    // --------------------------------------------------

    private void validatePassword(
            String password) {

        if (password == null ||
                password.isBlank()) {

            throw new ValidationException(
                    "Password cannot be empty"
            );
        }

        if (password.length() > 255) {

            throw new ValidationException(
                    "Password cannot exceed 255 characters"
            );
        }
    }

    // --------------------------------------------------
    // EMAIL FORMAT VALIDATION
    // --------------------------------------------------

    private boolean isValidEmail(
            String email) {

        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }
}