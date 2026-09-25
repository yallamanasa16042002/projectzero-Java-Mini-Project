package com.instagram.dao;

import com.instagram.model.User;

import java.util.List;

public interface UserDAO {

    int createUser(User user);

    User findByUsername(String username);

    User findByEmail(String email);

    User findActiveUserForLogin(String identifier);

    boolean updateUser(User user);

    boolean changePassword(int userId, String newPassword);

    List<User> searchUsersByUsername(String username);
}