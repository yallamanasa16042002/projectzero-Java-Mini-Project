package com.instagram.service;

import com.instagram.model.Post;

import java.util.List;

public interface PostService {

    int createPost(Post post);

    List<Post> getPostsByUserId(int userId);

    List<Post> getAllActivePosts();

    Post getPostById(int postId);

    boolean updatePost(Post post);

    boolean deletePost(int postId, int userId);
}