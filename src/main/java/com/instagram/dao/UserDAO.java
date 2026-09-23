package com.instagram.dao;

import com.instagram.model.User;

import java.util.List;

public interface UserDAO {

    boolean addUser(User user);
    User login(String username, String password);

    User getUserById(int userId);

    User getUserByUsername(String username);

    //User getUserByEmail(String email);

    List<User> getAllUsers();

    boolean updateUser(User user);

    boolean deleteuser(int userId);
}