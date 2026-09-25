package com.instagram.query;

public class PostQueries {

    // --------------------------------------------------
    // CREATE POST
    // --------------------------------------------------

    public static final String INSERT_POST =
            "INSERT INTO posts " +
                    "(user_id, caption, image_url, status) " +
                    "SELECT ?, ?, ?, ? " +
                    "FROM users " +
                    "WHERE user_id = ? " +
                    "AND status = 'ACTIVE'";

    // --------------------------------------------------
    // FIND POSTS BY USER
    // --------------------------------------------------

    public static final String FIND_POSTS_BY_USER_ID =
            "SELECT p.post_id, p.user_id, p.caption, " +
                    "p.image_url, p.status, p.created_at, p.updated_at, " +
                    "u.username " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.user_id " +
                    "WHERE p.user_id = ? " +
                    "AND p.status = 'ACTIVE' " +
                    "AND u.status = 'ACTIVE' " +
                    "ORDER BY p.created_at DESC";

    // --------------------------------------------------
    // FIND ALL ACTIVE POSTS
    // --------------------------------------------------

    public static final String FIND_ALL_ACTIVE_POSTS =
            "SELECT p.post_id, p.user_id, p.caption, " +
                    "p.image_url, p.status, p.created_at, p.updated_at, " +
                    "u.username " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.user_id " +
                    "WHERE p.status = 'ACTIVE' " +
                    "AND u.status = 'ACTIVE' " +
                    "ORDER BY p.created_at DESC";

    // --------------------------------------------------
    // FIND POST BY ID
    // --------------------------------------------------

    public static final String FIND_POST_BY_ID =
            "SELECT p.post_id, p.user_id, p.caption, " +
                    "p.image_url, p.status, p.created_at, p.updated_at, " +
                    "u.username " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.user_id " +
                    "WHERE p.post_id = ? " +
                    "AND p.status = 'ACTIVE' " +
                    "AND u.status = 'ACTIVE'";

    // --------------------------------------------------
    // UPDATE OWN POST
    // --------------------------------------------------

    public static final String UPDATE_POST =
            "UPDATE posts " +
                    "SET caption = ?, image_url = ? " +
                    "WHERE post_id = ? " +
                    "AND user_id = ? " +
                    "AND status = 'ACTIVE' " +
                    "AND EXISTS (" +
                    "SELECT 1 FROM users " +
                    "WHERE user_id = ? " +
                    "AND status = 'ACTIVE'" +
                    ")";

    // --------------------------------------------------
    // DELETE OWN POST
    // --------------------------------------------------

    public static final String DELETE_POST =
            "UPDATE posts " +
                    "SET status = 'DELETED' " +
                    "WHERE post_id = ? " +
                    "AND user_id = ? " +
                    "AND status = 'ACTIVE' " +
                    "AND EXISTS (" +
                    "SELECT 1 FROM users " +
                    "WHERE user_id = ? " +
                    "AND status = 'ACTIVE'" +
                    ")";
}