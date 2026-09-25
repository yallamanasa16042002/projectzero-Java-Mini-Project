package com.instagram.util;

import com.instagram.model.Post;
import com.instagram.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public final class ConsoleUI {

    private static final Logger uiLogger =
            LoggerFactory.getLogger("com.instagram.ui");

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(
                    "dd MMM yyyy, hh:mm a"
            );

    private ConsoleUI() {
    }

    // --------------------------------------------------
    // APPLICATION TITLE
    // --------------------------------------------------

    public static void applicationTitle() {

        uiLogger.info("");

        uiLogger.info(
                "============================================================"
        );

        uiLogger.info(
                "                 INSTAGRAM JAVA APPLICATION"
        );

        uiLogger.info(
                "============================================================"
        );
    }

    // --------------------------------------------------
    // SECTION HEADER
    // --------------------------------------------------

    public static void section(
            String title) {

        uiLogger.info("");

        uiLogger.info(
                "------------------------------------------------------------"
        );

        uiLogger.info(
                "                     {}",
                title
        );

        uiLogger.info(
                "------------------------------------------------------------"
        );
    }

    // --------------------------------------------------
    // MENU ITEM
    // --------------------------------------------------

    public static void menuItem(
            int number,
            String text) {

        uiLogger.info(
                "  {}. {}",
                number,
                text
        );
    }

    // --------------------------------------------------
    // PROMPT
    // --------------------------------------------------

    public static void prompt(
            String message) {

        uiLogger.info(
                message
        );
    }

    // --------------------------------------------------
    // REQUIRED INPUT
    // --------------------------------------------------

    public static String readRequired(
            Scanner scanner,
            String message) {

        while (true) {

            prompt(
                    message
            );

            String input =
                    scanner.nextLine().trim();

            if (!input.isBlank()) {

                return input;
            }

            warning(
                    "This field cannot be empty."
            );
        }
    }

    // --------------------------------------------------
    // OPTIONAL INPUT
    // --------------------------------------------------

    public static String readOptional(
            Scanner scanner,
            String message) {

        prompt(
                message
        );

        return scanner.nextLine().trim();
    }

    // --------------------------------------------------
    // READ SELECTION
    // --------------------------------------------------

    public static int readSelection(
            Scanner scanner,
            String message,
            int maximum) {

        while (true) {

            prompt(
                    message
            );

            String input =
                    scanner.nextLine().trim();

            try {

                int value =
                        Integer.parseInt(input);

                if (value >= 1 &&
                        value <= maximum) {

                    return value;
                }

            } catch (NumberFormatException e) {

                // Invalid input is handled below.
            }

            warning(
                    "Please enter a number between 1 and "
                            + maximum
            );
        }
    }

    // --------------------------------------------------
    // SUCCESS MESSAGE
    // --------------------------------------------------

    public static void success(
            String message) {

        uiLogger.info(
                "[SUCCESS] {}",
                message
        );
    }

    // --------------------------------------------------
    // INFORMATION MESSAGE
    // --------------------------------------------------

    public static void info(
            String message) {

        uiLogger.info(
                "[INFO] {}",
                message
        );
    }

    // --------------------------------------------------
    // WARNING MESSAGE
    // --------------------------------------------------

    public static void warning(
            String message) {

        uiLogger.warn(
                "[WARNING] {}",
                message
        );
    }

    // --------------------------------------------------
    // ERROR MESSAGE
    // --------------------------------------------------

    public static void error(
            String message) {

        uiLogger.error(
                "[ERROR] {}",
                message
        );
    }

    // --------------------------------------------------
    // DISPLAY USER ACCOUNT
    // --------------------------------------------------

    public static void displayUserAccount(
            User user) {

        section(
                "MY ACCOUNT"
        );

        uiLogger.info(
                "  Username     : @{}",
                user.getUsername()
        );

        uiLogger.info(
                "  Email        : {}",
                user.getEmail()
        );

        uiLogger.info(
                "  Role         : {}",
                user.getRole()
        );

        uiLogger.info(
                "  Account      : {}",
                user.getStatus()
        );

        if (user.getCreatedAt() != null) {

            uiLogger.info(
                    "  Joined       : {}",
                    formatDate(
                            user.getCreatedAt()
                    )
            );
        }

        uiLogger.info(
                "------------------------------------------------------------"
        );
    }

    // --------------------------------------------------
    // DISPLAY USER RESULT
    // --------------------------------------------------

    public static void displayUserResult(
            int number,
            User user) {

        uiLogger.info(
                "  {}. @{}",
                number,
                user.getUsername()
        );

        uiLogger.info(
                "     Status: {}",
                user.getStatus()
        );
    }

    // --------------------------------------------------
    // DISPLAY POST SUMMARY
    // --------------------------------------------------

    public static void displayPostSummary(
            int number,
            Post post) {

        String username =
                post.getUsername() == null
                        ? "unknown"
                        : post.getUsername();

        uiLogger.info(
                "  {}. @{}",
                number,
                username
        );

        displayPostContent(
                post
        );

        if (post.getCreatedAt() != null) {

            uiLogger.info(
                    "     Posted: {}",
                    formatDate(
                            post.getCreatedAt()
                    )
            );
        }

        uiLogger.info("");
    }

    // --------------------------------------------------
    // DISPLAY POST CARD
    // --------------------------------------------------

    public static void displayPostCard(
            Post post) {

        String username =
                post.getUsername() == null
                        ? "unknown"
                        : post.getUsername();

        uiLogger.info(
                "+----------------------------------------------------------+"
        );

        uiLogger.info(
                "  @{}",
                username
        );

        uiLogger.info("");

        displayPostContent(
                post
        );

        if (post.getCreatedAt() != null) {

            uiLogger.info("");

            uiLogger.info(
                    "  Posted: {}",
                    formatDate(
                            post.getCreatedAt()
                    )
            );
        }

        uiLogger.info(
                "+----------------------------------------------------------+"
        );

        uiLogger.info("");
    }

    // --------------------------------------------------
    // DISPLAY POST CONTENT
    // --------------------------------------------------

    private static void displayPostContent(
            Post post) {

        if (post.getCaption() != null &&
                !post.getCaption().isBlank()) {

            uiLogger.info(
                    "  {}",
                    post.getCaption()
            );
        }

        if (post.getImageUrl() != null &&
                !post.getImageUrl().isBlank()) {

            uiLogger.info(
                    "  Image: {}",
                    post.getImageUrl()
            );
        }
    }

    // --------------------------------------------------
    // NO RESULTS
    // --------------------------------------------------

    public static void noResults(
            String message) {

        uiLogger.info("");

        uiLogger.info(
                "  No results found."
        );

        uiLogger.info(
                "  {}",
                message
        );

        uiLogger.info("");
    }

    // --------------------------------------------------
    // FORMAT DATE
    // --------------------------------------------------

    private static String formatDate(
            LocalDateTime dateTime) {

        return dateTime.format(
                DATE_TIME_FORMATTER
        );
    }
}