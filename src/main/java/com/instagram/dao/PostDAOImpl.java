package com.instagram.dao;

import com.instagram.exception.DataAccessException;
import com.instagram.model.Post;
import com.instagram.query.PostQueries;
import com.instagram.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PostDAOImpl implements PostDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(PostDAOImpl.class);

    // --------------------------------------------------
    // CREATE POST
    // --------------------------------------------------

    @Override
    public int createPost(
            Post post) {

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             PostQueries.INSERT_POST,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setInt(
                    1,
                    post.getUserId()
            );

            if (post.getCaption() == null) {

                statement.setNull(
                        2,
                        java.sql.Types.VARCHAR
                );

            } else {

                statement.setString(
                        2,
                        post.getCaption()
                );
            }

            if (post.getImageUrl() == null) {

                statement.setNull(
                        3,
                        java.sql.Types.VARCHAR
                );

            } else {

                statement.setString(
                        3,
                        post.getImageUrl()
                );
            }

            statement.setString(
                    4,
                    "ACTIVE"
            );

            statement.setInt(
                    5,
                    post.getUserId()
            );

            int rows =
                    statement.executeUpdate();

            if (rows == 0) {

                logger.warn(
                        "Post creation failed: user ID {} is invalid or inactive",
                        post.getUserId()
                );

                return 0;
            }

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    int postId =
                            resultSet.getInt(1);

                    logger.info(
                            "Post created successfully with ID: {} for user ID: {}",
                            postId,
                            post.getUserId()
                    );

                    return postId;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Database error while creating post",
                    e
            );

            throw new DataAccessException(
                    "Error while creating post",
                    e
            );
        }

        return 0;
    }

    // --------------------------------------------------
    // FIND POSTS BY USER
    // --------------------------------------------------

    @Override
    public List<Post> findPostsByUserId(
            int userId) {

        List<Post> posts =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             PostQueries.FIND_POSTS_BY_USER_ID
                     )) {

            statement.setInt(
                    1,
                    userId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    posts.add(
                            mapPost(resultSet)
                    );
                }
            }

            logger.info(
                    "Found {} active posts for user ID: {}",
                    posts.size(),
                    userId
            );

        } catch (SQLException e) {

            logger.error(
                    "Database error while finding posts for user ID: {}",
                    userId,
                    e
            );

            throw new DataAccessException(
                    "Error while finding user posts",
                    e
            );
        }

        return posts;
    }

    // --------------------------------------------------
    // FIND ALL ACTIVE POSTS
    // --------------------------------------------------

    @Override
    public List<Post> findAllActivePosts() {

        List<Post> posts =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             PostQueries.FIND_ALL_ACTIVE_POSTS
                     );

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                posts.add(
                        mapPost(resultSet)
                );
            }

            logger.info(
                    "Found {} active posts",
                    posts.size()
            );

        } catch (SQLException e) {

            logger.error(
                    "Database error while finding active posts",
                    e
            );

            throw new DataAccessException(
                    "Error while finding active posts",
                    e
            );
        }

        return posts;
    }

    // --------------------------------------------------
    // FIND POST BY ID
    // --------------------------------------------------

    @Override
    public Post findPostById(
            int postId) {

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             PostQueries.FIND_POST_BY_ID
                     )) {

            statement.setInt(
                    1,
                    postId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info(
                            "Post found successfully with ID: {}",
                            postId
                    );

                    return mapPost(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Database error while finding post ID: {}",
                    postId,
                    e
            );

            throw new DataAccessException(
                    "Error while finding post",
                    e
            );
        }

        logger.info(
                "No active post found with ID: {}",
                postId
        );

        return null;
    }

    // --------------------------------------------------
    // UPDATE OWN POST
    // --------------------------------------------------

    @Override
    public boolean updatePost(
            Post post) {

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             PostQueries.UPDATE_POST
                     )) {

            if (post.getCaption() == null) {

                statement.setNull(
                        1,
                        java.sql.Types.VARCHAR
                );

            } else {

                statement.setString(
                        1,
                        post.getCaption()
                );
            }

            if (post.getImageUrl() == null) {

                statement.setNull(
                        2,
                        java.sql.Types.VARCHAR
                );

            } else {

                statement.setString(
                        2,
                        post.getImageUrl()
                );
            }

            statement.setInt(
                    3,
                    post.getPostId()
            );

            statement.setInt(
                    4,
                    post.getUserId()
            );

            statement.setInt(
                    5,
                    post.getUserId()
            );

            int rows =
                    statement.executeUpdate();

            if (rows == 1) {

                logger.info(
                        "Post updated successfully with ID: {} by user ID: {}",
                        post.getPostId(),
                        post.getUserId()
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Database error while updating post ID: {}",
                    post.getPostId(),
                    e
            );

            throw new DataAccessException(
                    "Error while updating post",
                    e
            );
        }

        logger.warn(
                "Post update failed for post ID: {} and user ID: {}",
                post.getPostId(),
                post.getUserId()
        );

        return false;
    }

    // --------------------------------------------------
    // DELETE OWN POST
    // --------------------------------------------------

    @Override
    public boolean deletePost(
            int postId,
            int userId) {

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             PostQueries.DELETE_POST
                     )) {

            statement.setInt(
                    1,
                    postId
            );

            statement.setInt(
                    2,
                    userId
            );

            statement.setInt(
                    3,
                    userId
            );

            int rows =
                    statement.executeUpdate();

            if (rows == 1) {

                logger.info(
                        "Post deleted successfully with ID: {} by user ID: {}",
                        postId,
                        userId
                );

                return true;
            }

        } catch (SQLException e) {

            logger.error(
                    "Database error while deleting post ID: {}",
                    postId,
                    e
            );

            throw new DataAccessException(
                    "Error while deleting post",
                    e
            );
        }

        logger.warn(
                "Post deletion failed for post ID: {} and user ID: {}",
                postId,
                userId
        );

        return false;
    }

    // --------------------------------------------------
    // MAP RESULT SET TO POST
    // --------------------------------------------------

    private Post mapPost(
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

        Post post =
                new Post(
                        resultSet.getInt(
                                "post_id"
                        ),
                        resultSet.getInt(
                                "user_id"
                        ),
                        resultSet.getString(
                                "caption"
                        ),
                        resultSet.getString(
                                "image_url"
                        ),
                        resultSet.getString(
                                "status"
                        ),
                        createdAt != null
                                ? createdAt.toLocalDateTime()
                                : null,
                        updatedAt != null
                                ? updatedAt.toLocalDateTime()
                                : null
                );

        post.setUsername(
                resultSet.getString(
                        "username"
                )
        );

        return post;
    }
}