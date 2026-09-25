package com.instagram.service;

import com.instagram.dao.PostDAO;
import com.instagram.dao.PostDAOImpl;
import com.instagram.exception.ValidationException;
import com.instagram.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PostServiceImpl implements PostService {

    private static final Logger logger =
            LoggerFactory.getLogger(PostServiceImpl.class);

    private static final int MAX_CAPTION_LENGTH = 2200;
    private static final int MAX_IMAGE_URL_LENGTH = 500;

    private final PostDAO postDAO;

    public PostServiceImpl() {
        this.postDAO = new PostDAOImpl();
    }

    // --------------------------------------------------
    // CREATE POST
    // --------------------------------------------------

    @Override
    public int createPost(
            Post post) {

        if (post == null) {

            logger.warn(
                    "Post creation failed: post details are null"
            );

            throw new ValidationException(
                    "Post details cannot be null"
            );
        }

        if (post.getUserId() <= 0) {

            logger.warn(
                    "Post creation failed: invalid user ID"
            );

            throw new ValidationException(
                    "Invalid user ID"
            );
        }

        normalizeContent(
                post
        );

        validateContent(
                post
        );

        post.setStatus(
                "ACTIVE"
        );

        int postId =
                postDAO.createPost(
                        post
                );

        if (postId <= 0) {

            logger.warn(
                    "Post creation failed for user ID: {}",
                    post.getUserId()
            );

            throw new ValidationException(
                    "Post cannot be created. User may be invalid or inactive."
            );
        }

        logger.info(
                "Post creation successful with ID: {}",
                postId
        );

        return postId;
    }

    // --------------------------------------------------
    // GET POSTS BY USER
    // --------------------------------------------------

    @Override
    public List<Post> getPostsByUserId(
            int userId) {

        if (userId <= 0) {

            logger.warn(
                    "Find posts failed: invalid user ID"
            );

            throw new ValidationException(
                    "Invalid user ID"
            );
        }

        return postDAO.findPostsByUserId(
                userId
        );
    }

    // --------------------------------------------------
    // GET ALL ACTIVE POSTS
    // --------------------------------------------------

    @Override
    public List<Post> getAllActivePosts() {

        return postDAO.findAllActivePosts();
    }

    // --------------------------------------------------
    // GET POST DETAILS
    // --------------------------------------------------

    @Override
    public Post getPostById(
            int postId) {

        if (postId <= 0) {

            logger.warn(
                    "Find post failed: invalid post ID"
            );

            throw new ValidationException(
                    "Invalid post ID"
            );
        }

        return postDAO.findPostById(
                postId
        );
    }

    // --------------------------------------------------
    // UPDATE POST
    // --------------------------------------------------

    @Override
    public boolean updatePost(
            Post post) {

        if (post == null) {

            logger.warn(
                    "Post update failed: post details are null"
            );

            throw new ValidationException(
                    "Post details cannot be null"
            );
        }

        if (post.getPostId() <= 0) {

            throw new ValidationException(
                    "Invalid post ID"
            );
        }

        if (post.getUserId() <= 0) {

            throw new ValidationException(
                    "Invalid user ID"
            );
        }

        normalizeContent(
                post
        );

        validateContent(
                post
        );

        boolean updated =
                postDAO.updatePost(
                        post
                );

        if (!updated) {

            logger.warn(
                    "Post update failed for post ID: {} and user ID: {}",
                    post.getPostId(),
                    post.getUserId()
            );

            throw new ValidationException(
                    "Post update failed. Post may not exist or you may not be the owner."
            );
        }

        logger.info(
                "Post updated successfully with ID: {}",
                post.getPostId()
        );

        return true;
    }

    // --------------------------------------------------
    // DELETE POST
    // --------------------------------------------------

    @Override
    public boolean deletePost(
            int postId,
            int userId) {

        if (postId <= 0) {

            throw new ValidationException(
                    "Invalid post ID"
            );
        }

        if (userId <= 0) {

            throw new ValidationException(
                    "Invalid user ID"
            );
        }

        boolean deleted =
                postDAO.deletePost(
                        postId,
                        userId
                );

        if (!deleted) {

            logger.warn(
                    "Post deletion failed for post ID: {} and user ID: {}",
                    postId,
                    userId
            );

            throw new ValidationException(
                    "Post deletion failed. Post may not exist or you may not be the owner."
            );
        }

        logger.info(
                "Post deleted successfully with ID: {}",
                postId
        );

        return true;
    }

    // --------------------------------------------------
    // NORMALIZE POST CONTENT
    // --------------------------------------------------

    private void normalizeContent(
            Post post) {

        if (post.getCaption() != null) {

            String caption =
                    post.getCaption().trim();

            post.setCaption(
                    caption.isBlank()
                            ? null
                            : caption
            );
        }

        if (post.getImageUrl() != null) {

            String imageUrl =
                    post.getImageUrl().trim();

            post.setImageUrl(
                    imageUrl.isBlank()
                            ? null
                            : imageUrl
            );
        }
    }

    // --------------------------------------------------
    // VALIDATE POST CONTENT
    // --------------------------------------------------

    private void validateContent(
            Post post) {

        boolean noCaption =
                post.getCaption() == null;

        boolean noImage =
                post.getImageUrl() == null;

        if (noCaption && noImage) {

            throw new ValidationException(
                    "A post must contain a caption, an image URL, or both."
            );
        }

        if (post.getCaption() != null &&
                post.getCaption().length()
                        > MAX_CAPTION_LENGTH) {

            throw new ValidationException(
                    "Caption cannot exceed "
                            + MAX_CAPTION_LENGTH
                            + " characters"
            );
        }

        if (post.getImageUrl() != null &&
                post.getImageUrl().length()
                        > MAX_IMAGE_URL_LENGTH) {

            throw new ValidationException(
                    "Image URL cannot exceed "
                            + MAX_IMAGE_URL_LENGTH
                            + " characters"
            );
        }
    }
}