package com.instagram.controller;

import com.instagram.model.User;
import com.instagram.service.PostService;
import com.instagram.service.PostServiceImpl;
import com.instagram.service.UserService;
import com.instagram.service.UserServiceImpl;
import com.instagram.util.ConsoleUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class MainController {

    private static final Logger logger =
            LoggerFactory.getLogger(MainController.class);

    private final Scanner scanner;

    private final UserController userController;

    private final PostController postController;

    private User currentUser;

    public MainController() {

        this.scanner =
                new Scanner(System.in);

        // --------------------------------------------------
        // CREATE SERVICES
        // --------------------------------------------------

        UserService userService =
                new UserServiceImpl();

        PostService postService =
                new PostServiceImpl();

        // --------------------------------------------------
        // CONNECT CONTROLLERS
        // --------------------------------------------------

        this.userController =
                new UserController(
                        userService
                );

        this.postController =
                new PostController(
                        postService,
                        userService
                );

        logger.info(
                "MainController initialized"
        );

        logger.info(
                "UserController connected successfully"
        );

        logger.info(
                "PostController connected successfully"
        );
    }

    // --------------------------------------------------
    // START APPLICATION
    // --------------------------------------------------

    public void start() {

        ConsoleUI.applicationTitle();

        boolean running = true;

        while (running) {

            if (currentUser == null) {

                running =
                        showGuestMenu();

            } else {

                running =
                        showUserMenu();
            }
        }

        scanner.close();

        logger.info(
                "Application terminated"
        );
    }

    // --------------------------------------------------
    // GUEST MENU
    // --------------------------------------------------

    private boolean showGuestMenu() {

        ConsoleUI.section(
                "WELCOME"
        );

        ConsoleUI.menuItem(
                1,
                "Register"
        );

        ConsoleUI.menuItem(
                2,
                "Login"
        );

        ConsoleUI.menuItem(
                3,
                "Exit"
        );

        ConsoleUI.info("");

        String choice =
                ConsoleUI.readRequired(
                        scanner,
                        "  Enter your choice: "
                );

        switch (choice) {

            case "1":

                userController.registerUser(
                        scanner
                );

                return true;

            case "2":

                User loggedInUser =
                        userController.loginUser(
                                scanner
                        );

                if (loggedInUser != null) {

                    currentUser =
                            loggedInUser;
                }

                return true;

            case "3":

                ConsoleUI.info(
                        "Thank you for using Instagram Java Application."
                );

                return false;

            default:

                ConsoleUI.warning(
                        "Invalid menu option."
                );

                return true;
        }
    }

    // --------------------------------------------------
    // USER MENU
    // --------------------------------------------------

    private boolean showUserMenu() {

        ConsoleUI.section(
                "HOME"
        );

        ConsoleUI.info(
                "Signed in as @"
                        + currentUser.getUsername()
        );

        ConsoleUI.info("");

        ConsoleUI.menuItem(
                1,
                "My Account"
        );

        ConsoleUI.menuItem(
                2,
                "Search Users"
        );

        ConsoleUI.menuItem(
                3,
                "Update Account"
        );

        ConsoleUI.menuItem(
                4,
                "Change Password"
        );

        ConsoleUI.menuItem(
                5,
                "Post Management"
        );

        ConsoleUI.menuItem(
                6,
                "Logout"
        );

        ConsoleUI.menuItem(
                7,
                "Exit"
        );

        ConsoleUI.info("");

        String choice =
                ConsoleUI.readRequired(
                        scanner,
                        "  Enter your choice: "
                );

        switch (choice) {

            case "1":

                userController.viewMyAccount(
                        currentUser
                );

                return true;

            case "2":

                userController.searchUsers(
                        scanner
                );

                return true;

            case "3":

                User updatedUser =
                        userController.updateMyAccount(
                                scanner,
                                currentUser
                        );

                if (updatedUser != null) {

                    currentUser =
                            updatedUser;
                }

                return true;

            case "4":

                userController.changePassword(
                        scanner,
                        currentUser
                );

                return true;

            case "5":

                postController.startPostMenu(
                        scanner,
                        currentUser
                );

                return true;

            case "6":

                logger.info(
                        "User logged out: {}",
                        currentUser.getUsername()
                );

                ConsoleUI.success(
                        "You have been logged out."
                );

                currentUser = null;

                return true;

            case "7":

                ConsoleUI.info(
                        "Thank you for using Instagram Java Application."
                );

                return false;

            default:

                ConsoleUI.warning(
                        "Invalid menu option."
                );

                return true;
        }
    }
}