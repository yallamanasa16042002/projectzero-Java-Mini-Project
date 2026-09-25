package com.instagram.controller;

import com.instagram.exception.ApplicationException;
import com.instagram.exception.DataAccessException;
import com.instagram.exception.ValidationException;
import com.instagram.model.User;
import com.instagram.service.UserService;
import com.instagram.util.ConsoleUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(
            UserService userService) {

        this.userService = userService;
    }

    // --------------------------------------------------
    // REGISTER USER
    // --------------------------------------------------

    public void registerUser(
            Scanner scanner) {

        ConsoleUI.section(
                "CREATE ACCOUNT"
        );

        String username =
                ConsoleUI.readRequired(
                        scanner,
                        "  Enter username: "
                );

        String email =
                ConsoleUI.readRequired(
                        scanner,
                        "  Enter email: "
                );

        String password =
                ConsoleUI.readRequired(
                        scanner,
                        "  Enter password: "
                );

        User user =
                new User(
                        0,
                        username,
                        email,
                        password,
                        "ACTIVE",
                        "USER",
                        null,
                        null
                );

        try {

            userService.registerUser(
                    user
            );

            ConsoleUI.success(
                    "Account created successfully."
            );

            ConsoleUI.info(
                    "You can now log in using your username or email."
            );

            logger.info(
                    "User registration completed for username: {}",
                    username
            );

        } catch (ValidationException e) {

            ConsoleUI.warning(
                    e.getMessage()
            );

            logger.warn(
                    "User registration validation failed"
            );

        } catch (DataAccessException e) {

            ConsoleUI.error(
                    e.getMessage()
            );

            logger.warn(
                    "User registration database validation failed"
            );

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Registration failed. Please try again."
            );

            logger.error(
                    "User registration failed",
                    e
            );
        }
    }

    // --------------------------------------------------
    // LOGIN USER
    // --------------------------------------------------

    public User loginUser(
            Scanner scanner) {

        ConsoleUI.section(
                "LOGIN"
        );

        String identifier =
                ConsoleUI.readRequired(
                        scanner,
                        "  Username or email: "
                );

        String password =
                ConsoleUI.readRequired(
                        scanner,
                        "  Password: "
                );

        try {

            User user =
                    userService.loginUser(
                            identifier,
                            password
                    );

            ConsoleUI.success(
                    "Welcome back, @"
                            + user.getUsername()
            );

            logger.info(
                    "User login completed successfully for: {}",
                    identifier
            );

            return user;

        } catch (ValidationException e) {

            ConsoleUI.warning(
                    e.getMessage()
            );

            logger.warn(
                    "User login validation failed for identifier: {}",
                    identifier
            );

        } catch (DataAccessException e) {

            ConsoleUI.error(
                    "Login could not be completed. Please try again."
            );

            logger.error(
                    "User login database operation failed",
                    e
            );

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Login failed. Please try again."
            );

            logger.error(
                    "User login failed",
                    e
            );
        }

        return null;
    }

    // --------------------------------------------------
    // VIEW MY ACCOUNT
    // --------------------------------------------------

    public void viewMyAccount(
            User currentUser) {

        if (currentUser == null) {

            ConsoleUI.warning(
                    "No user is currently logged in."
            );

            return;
        }

        ConsoleUI.displayUserAccount(
                currentUser
        );
    }

    // --------------------------------------------------
    // SEARCH USERS
    // --------------------------------------------------

    public void searchUsers(
            Scanner scanner) {

        ConsoleUI.section(
                "SEARCH USERS"
        );

        String username =
                ConsoleUI.readRequired(
                        scanner,
                        "  Search by username: "
                );

        try {

            List<User> users =
                    userService.searchUsersByUsername(
                            username
                    );

            if (users.isEmpty()) {

                ConsoleUI.noResults(
                        "No matching username was found."
                );

                return;
            }

            ConsoleUI.info(
                    "Matching users:"
            );

            ConsoleUI.info("");

            for (int i = 0;
                 i < users.size();
                 i++) {

                ConsoleUI.displayUserResult(
                        i + 1,
                        users.get(i)
                );
            }

        } catch (ValidationException e) {

            ConsoleUI.warning(
                    e.getMessage()
            );

        } catch (DataAccessException e) {

            ConsoleUI.error(
                    "User search could not be completed."
            );

            logger.error(
                    "User search database operation failed",
                    e
            );

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "User search failed."
            );

            logger.error(
                    "User search failed",
                    e
            );
        }
    }

    // --------------------------------------------------
    // UPDATE MY ACCOUNT
    // --------------------------------------------------

    public User updateMyAccount(
            Scanner scanner,
            User currentUser) {

        if (currentUser == null) {

            ConsoleUI.warning(
                    "No user is currently logged in."
            );

            return null;
        }

        ConsoleUI.section(
                "UPDATE ACCOUNT"
        );

        ConsoleUI.info(
                "Current username: @"
                        + currentUser.getUsername()
        );

        ConsoleUI.info(
                "Current email: "
                        + currentUser.getEmail()
        );

        ConsoleUI.info(
                "Press Enter on a field to keep the current value."
        );

        String username =
                ConsoleUI.readOptional(
                        scanner,
                        "  New username: "
                );

        String email =
                ConsoleUI.readOptional(
                        scanner,
                        "  New email: "
                );

        if (username.isBlank()) {

            username =
                    currentUser.getUsername();
        }

        if (email.isBlank()) {

            email =
                    currentUser.getEmail();
        }

        User updatedUser =
                new User(
                        currentUser.getUserId(),
                        username,
                        email,
                        currentUser.getPasswordHash(),
                        currentUser.getStatus(),
                        currentUser.getRole(),
                        currentUser.getCreatedAt(),
                        currentUser.getUpdatedAt()
                );

        try {

            userService.updateUser(
                    updatedUser
            );

            ConsoleUI.success(
                    "Account updated successfully."
            );

            return updatedUser;

        } catch (ValidationException e) {

            ConsoleUI.warning(
                    e.getMessage()
            );

        } catch (DataAccessException e) {

            ConsoleUI.error(
                    e.getMessage()
            );

            logger.warn(
                    "Account update database validation failed"
            );

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Account update failed."
            );

            logger.error(
                    "User account update failed",
                    e
            );
        }

        return null;
    }

    // --------------------------------------------------
    // CHANGE PASSWORD
    // --------------------------------------------------

    public boolean changePassword(
            Scanner scanner,
            User currentUser) {

        if (currentUser == null) {

            ConsoleUI.warning(
                    "No user is currently logged in."
            );

            return false;
        }

        ConsoleUI.section(
                "CHANGE PASSWORD"
        );

        String newPassword =
                ConsoleUI.readRequired(
                        scanner,
                        "  Enter new password: "
                );

        try {

            boolean changed =
                    userService.changePassword(
                            currentUser.getUserId(),
                            newPassword
                    );

            if (changed) {

                currentUser.setPasswordHash(
                        newPassword
                );

                ConsoleUI.success(
                        "Password changed successfully."
                );

                return true;
            }

        } catch (ValidationException e) {

            ConsoleUI.warning(
                    e.getMessage()
            );

        } catch (DataAccessException e) {

            ConsoleUI.error(
                    "Password change could not be completed."
            );

            logger.error(
                    "Password change database operation failed",
                    e
            );

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Password change failed."
            );

            logger.error(
                    "Password change failed",
                    e
            );
        }

        return false;
    }
}