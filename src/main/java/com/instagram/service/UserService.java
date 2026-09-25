package com.instagram.service;

import com.instagram.model.User;

import java.util.List;

public interface UserService {

    int registerUser(User user);

    User loginUser(String identifier, String password);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    boolean updateUser(User user);

    boolean changePassword(int userId, String newPassword);

    List<User> searchUsersByUsername(String username);
}