package com.instagram.dao;

import com.instagram.model.Post;

import java.util.List;

public interface PostDAO {

    int createPost(Post post);

    List<Post> findPostsByUserId(int userId);

    List<Post> findAllActivePosts();

    Post findPostById(int postId);

    boolean updatePost(Post post);

    boolean deletePost(int postId, int userId);
}