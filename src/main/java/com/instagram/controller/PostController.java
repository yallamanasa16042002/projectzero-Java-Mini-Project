package com.instagram.controller;

import com.instagram.exception.ApplicationException;
import com.instagram.exception.DataAccessException;
import com.instagram.exception.ValidationException;
import com.instagram.model.Post;
import com.instagram.model.User;
import com.instagram.service.PostService;
import com.instagram.service.UserService;
import com.instagram.util.ConsoleUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class PostController {

    private static final Logger logger =
            LoggerFactory.getLogger(PostController.class);

    private final PostService postService;
    private final UserService userService;

    public PostController(
            PostService postService,
            UserService userService) {

        this.postService = postService;
        this.userService = userService;
    }

    // --------------------------------------------------
    // POST MENU
    // --------------------------------------------------

    public void startPostMenu(
            Scanner scanner,
            User currentUser) {

        if (currentUser == null) {

            ConsoleUI.warning(
                    "Please log in before using Post Management."
            );

            return;
        }

        boolean running = true;

        while (running) {

            ConsoleUI.section(
                    "POST MANAGEMENT"
            );

            ConsoleUI.info(
                    "Logged in as @"
                            + currentUser.getUsername()
            );

            ConsoleUI.info("");

            ConsoleUI.menuItem(
                    1,
                    "Create Post"
            );

            ConsoleUI.menuItem(
                    2,
                    "My Posts"
            );

            ConsoleUI.menuItem(
                    3,
                    "Explore Posts"
            );

            ConsoleUI.menuItem(
                    4,
                    "View User Posts"
            );

            ConsoleUI.menuItem(
                    5,
                    "View Post Details"
            );

            ConsoleUI.menuItem(
                    6,
                    "Manage My Posts"
            );

            ConsoleUI.menuItem(
                    7,
                    "Back to User Menu"
            );

            ConsoleUI.info("");

            String choice =
                    ConsoleUI.readRequired(
                            scanner,
                            "  Enter your choice: "
                    );

            switch (choice) {

                case "1":
                    createPost(
                            scanner,
                            currentUser
                    );
                    break;

                case "2":
                    viewMyPosts(
                            currentUser
                    );
                    break;

                case "3":
                    explorePosts();
                    break;

                case "4":
                    viewUserPosts(
                            scanner
                    );
                    break;

                case "5":
                    viewPostDetails(
                            scanner
                    );
                    break;

                case "6":
                    manageMyPosts(
                            scanner,
                            currentUser
                    );
                    break;

                case "7":
                    running = false;
                    break;

                default:
                    ConsoleUI.warning(
                            "Invalid Post Management option."
                    );
            }
        }
    }

    // --------------------------------------------------
    // CREATE POST
    // --------------------------------------------------

    private void createPost(
            Scanner scanner,
            User currentUser) {

        ConsoleUI.section(
                "CREATE POST"
        );

        ConsoleUI.info(
                "Add a caption, an image URL, or both."
        );

        String caption =
                ConsoleUI.readOptional(
                        scanner,
                        "  Caption (optional): "
                );

        String imageUrl =
                ConsoleUI.readOptional(
                        scanner,
                        "  Image URL (optional): "
                );

        caption =
                caption.isBlank()
                        ? null
                        : caption;

        imageUrl =
                imageUrl.isBlank()
                        ? null
                        : imageUrl;

        Post post =
                new Post(
                        0,
                        currentUser.getUserId(),
                        caption,
                        imageUrl,
                        "ACTIVE",
                        null,
                        null
                );

        try {

            postService.createPost(
                    post
            );

            ConsoleUI.success(
                    "Your post was published successfully."
            );

        } catch (ValidationException e) {

            ConsoleUI.warning(
                    e.getMessage()
            );

        } catch (DataAccessException e) {

            ConsoleUI.error(
                    "Post could not be published."
            );

            logger.error(
                    "Post creation database operation failed",
                    e
            );

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Post creation failed."
            );

            logger.error(
                    "Post creation failed",
                    e
            );
        }
    }

    // --------------------------------------------------
    // VIEW MY POSTS
    // --------------------------------------------------

    private void viewMyPosts(
            User currentUser) {

        ConsoleUI.section(
                "MY POSTS"
        );

        try {

            List<Post> posts =
                    postService.getPostsByUserId(
                            currentUser.getUserId()
                    );

            if (posts.isEmpty()) {

                ConsoleUI.noResults(
                        "You have not published any active posts."
                );

                return;
            }

            for (Post post : posts) {

                ConsoleUI.displayPostCard(
                        post
                );
            }

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Unable to load your posts."
            );

            logger.error(
                    "My posts operation failed",
                    e
            );
        }
    }

    // --------------------------------------------------
    // EXPLORE POSTS
    // --------------------------------------------------

    private void explorePosts() {

        ConsoleUI.section(
                "EXPLORE POSTS"
        );

        try {

            List<Post> posts =
                    postService.getAllActivePosts();

            if (posts.isEmpty()) {

                ConsoleUI.noResults(
                        "There are no active posts to explore."
                );

                return;
            }

            for (int i = 0;
                 i < posts.size();
                 i++) {

                ConsoleUI.displayPostSummary(
                        i + 1,
                        posts.get(i)
                );
            }

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Unable to load Explore."
            );

            logger.error(
                    "Explore posts operation failed",
                    e
            );
        }
    }

    // --------------------------------------------------
    // VIEW USER POSTS
    // --------------------------------------------------

    private void viewUserPosts(
            Scanner scanner) {

        ConsoleUI.section(
                "VIEW USER POSTS"
        );

        String username =
                ConsoleUI.readRequired(
                        scanner,
                        "  Enter username: "
                );

        try {

            User user =
                    userService.findUserByUsername(
                            username
                    );

            if (user == null) {

                ConsoleUI.noResults(
                        "No user exists with username @"
                                + username
                );

                return;
            }

            List<Post> posts =
                    postService.getPostsByUserId(
                            user.getUserId()
                    );

            ConsoleUI.info(
                    "Posts published by @"
                            + user.getUsername()
            );

            ConsoleUI.info("");

            if (posts.isEmpty()) {

                ConsoleUI.noResults(
                        "This user has no active posts."
                );

                return;
            }

            for (Post post : posts) {

                ConsoleUI.displayPostCard(
                        post
                );
            }

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Unable to load user posts."
            );

            logger.error(
                    "User posts operation failed",
                    e
            );
        }
    }

    // --------------------------------------------------
    // VIEW POST DETAILS
    // --------------------------------------------------

    private void viewPostDetails(
            Scanner scanner) {

        ConsoleUI.section(
                "VIEW POST DETAILS"
        );

        try {

            List<Post> posts =
                    postService.getAllActivePosts();

            if (posts.isEmpty()) {

                ConsoleUI.noResults(
                        "There are no active posts."
                );

                return;
            }

            ConsoleUI.info(
                    "Select a post to open:"
            );

            ConsoleUI.info("");

            for (int i = 0;
                 i < posts.size();
                 i++) {

                ConsoleUI.displayPostSummary(
                        i + 1,
                        posts.get(i)
                );
            }

            int selection =
                    ConsoleUI.readSelection(
                            scanner,
                            "  Select post number: ",
                            posts.size()
                    );

            Post selectedPost =
                    posts.get(
                            selection - 1
                    );

            ConsoleUI.section(
                    "POST DETAILS"
            );

            ConsoleUI.displayPostCard(
                    selectedPost
            );

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Unable to load post details."
            );

            logger.error(
                    "Post details operation failed",
                    e
            );
        }
    }

    // --------------------------------------------------
    // MANAGE MY POSTS
    // --------------------------------------------------

    private void manageMyPosts(
            Scanner scanner,
            User currentUser) {

        ConsoleUI.section(
                "MANAGE MY POSTS"
        );

        try {

            List<Post> posts =
                    postService.getPostsByUserId(
                            currentUser.getUserId()
                    );

            if (posts.isEmpty()) {

                ConsoleUI.noResults(
                        "You have no active posts to manage."
                );

                return;
            }

            ConsoleUI.info(
                    "Select one of your posts:"
            );

            ConsoleUI.info("");

            for (int i = 0;
                 i < posts.size();
                 i++) {

                ConsoleUI.displayPostSummary(
                        i + 1,
                        posts.get(i)
                );
            }

            int selection =
                    ConsoleUI.readSelection(
                            scanner,
                            "  Select post number: ",
                            posts.size()
                    );

            Post selectedPost =
                    posts.get(
                            selection - 1
                    );

            manageSelectedPost(
                    scanner,
                    currentUser,
                    selectedPost
            );

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Unable to manage your posts."
            );

            logger.error(
                    "Manage posts operation failed",
                    e
            );
        }
    }

    // --------------------------------------------------
    // MANAGE SELECTED POST
    // --------------------------------------------------

    private void manageSelectedPost(
            Scanner scanner,
            User currentUser,
            Post selectedPost) {

        boolean running = true;

        while (running) {

            ConsoleUI.section(
                    "POST ACTIONS"
            );

            ConsoleUI.displayPostSummary(
                    1,
                    selectedPost
            );

            ConsoleUI.menuItem(
                    1,
                    "Edit Post"
            );

            ConsoleUI.menuItem(
                    2,
                    "Delete Post"
            );

            ConsoleUI.menuItem(
                    3,
                    "Back"
            );

            String choice =
                    ConsoleUI.readRequired(
                            scanner,
                            "  Enter your choice: "
                    );

            switch (choice) {

                case "1":

                    if (updatePost(
                            scanner,
                            currentUser,
                            selectedPost)) {

                        running = false;
                    }

                    break;

                case "2":

                    if (deletePost(
                            scanner,
                            currentUser,
                            selectedPost)) {

                        running = false;
                    }

                    break;

                case "3":

                    running = false;
                    break;

                default:

                    ConsoleUI.warning(
                            "Invalid post action."
                    );
            }
        }
    }

    // --------------------------------------------------
    // UPDATE POST
    // --------------------------------------------------

    private boolean updatePost(
            Scanner scanner,
            User currentUser,
            Post selectedPost) {

        ConsoleUI.section(
                "EDIT POST"
        );

        String caption =
                ConsoleUI.readOptional(
                        scanner,
                        "  New caption " +
                                "(Enter to keep current, NONE to remove): "
                );

        if (caption.isBlank()) {

            caption =
                    selectedPost.getCaption();

        } else if ("NONE".equalsIgnoreCase(
                caption)) {

            caption = null;
        }

        String imageUrl =
                ConsoleUI.readOptional(
                        scanner,
                        "  New image URL " +
                                "(Enter to keep current, NONE to remove): "
                );

        if (imageUrl.isBlank()) {

            imageUrl =
                    selectedPost.getImageUrl();

        } else if ("NONE".equalsIgnoreCase(
                imageUrl)) {

            imageUrl = null;
        }

        Post updatedPost =
                new Post(
                        selectedPost.getPostId(),
                        currentUser.getUserId(),
                        caption,
                        imageUrl,
                        selectedPost.getStatus(),
                        selectedPost.getCreatedAt(),
                        selectedPost.getUpdatedAt()
                );

        try {

            postService.updatePost(
                    updatedPost
            );

            ConsoleUI.success(
                    "Post updated successfully."
            );

            return true;

        } catch (ValidationException e) {

            ConsoleUI.warning(
                    e.getMessage()
            );

        } catch (DataAccessException e) {

            ConsoleUI.error(
                    "Post could not be updated."
            );

            logger.error(
                    "Post update database operation failed",
                    e
            );

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Post update failed."
            );

            logger.error(
                    "Post update failed",
                    e
            );
        }

        return false;
    }

    // --------------------------------------------------
    // DELETE POST
    // --------------------------------------------------

    private boolean deletePost(
            Scanner scanner,
            User currentUser,
            Post selectedPost) {

        ConsoleUI.section(
                "DELETE POST"
        );

        ConsoleUI.displayPostCard(
                selectedPost
        );

        String confirmation =
                ConsoleUI.readOptional(
                        scanner,
                        "  Type YES to delete this post: "
                );

        if (!"YES".equalsIgnoreCase(
                confirmation)) {

            ConsoleUI.info(
                    "Post deletion cancelled."
            );

            return false;
        }

        try {

            postService.deletePost(
                    selectedPost.getPostId(),
                    currentUser.getUserId()
            );

            ConsoleUI.success(
                    "Post deleted successfully."
            );

            return true;

        } catch (ValidationException e) {

            ConsoleUI.warning(
                    e.getMessage()
            );

        } catch (DataAccessException e) {

            ConsoleUI.error(
                    "Post could not be deleted."
            );

            logger.error(
                    "Post deletion database operation failed",
                    e
            );

        } catch (ApplicationException e) {

            ConsoleUI.error(
                    "Post deletion failed."
            );

            logger.error(
                    "Post deletion failed",
                    e
            );
        }

        return false;
    }
}