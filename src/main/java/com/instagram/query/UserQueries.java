package com.instagram.query;

public class UserQueries {

    // --------------------------------------------------
    // CREATE USER
    // --------------------------------------------------

    public static final String INSERT_USER =
            "INSERT INTO users " +
                    "(username, email, password_hash, status, role) " +
                    "VALUES (?, ?, ?, ?, ?)";

    // --------------------------------------------------
    // FIND USER BY USERNAME
    // --------------------------------------------------

    public static final String FIND_USER_BY_USERNAME =
            "SELECT user_id, username, email, password_hash, " +
                    "status, role, created_at, updated_at " +
                    "FROM users " +
                    "WHERE username = ?";

    // --------------------------------------------------
    // FIND USER BY EMAIL
    // --------------------------------------------------

    public static final String FIND_USER_BY_EMAIL =
            "SELECT user_id, username, email, password_hash, " +
                    "status, role, created_at, updated_at " +
                    "FROM users " +
                    "WHERE email = ?";

    // --------------------------------------------------
    // FIND ACTIVE USER FOR LOGIN
    // --------------------------------------------------

    public static final String FIND_ACTIVE_USER_FOR_LOGIN =
            "SELECT user_id, username, email, password_hash, " +
                    "status, role, created_at, updated_at " +
                    "FROM users " +
                    "WHERE (username = ? OR email = ?) " +
                    "AND status = 'ACTIVE'";

    // --------------------------------------------------
    // UPDATE USER ACCOUNT
    // --------------------------------------------------

    public static final String UPDATE_USER =
            "UPDATE users " +
                    "SET username = ?, email = ? " +
                    "WHERE user_id = ?";

    // --------------------------------------------------
    // CHANGE PASSWORD
    // --------------------------------------------------

    public static final String CHANGE_PASSWORD =
            "UPDATE users " +
                    "SET password_hash = ? " +
                    "WHERE user_id = ?";

    // --------------------------------------------------
    // SEARCH USERS
    // --------------------------------------------------

    public static final String SEARCH_USERS_BY_USERNAME =
            "SELECT user_id, username, email, password_hash, " +
                    "status, role, created_at, updated_at " +
                    "FROM users " +
                    "WHERE username LIKE ? " +
                    "ORDER BY username";
}