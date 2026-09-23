package com.instagram.service;

import com.instagram.dao.UserDAO;
import com.instagram.dao.UserDAOImpl;
import com.instagram.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public boolean registerUser(User user) {

        if (user == null) {
            return false;
        }

        if (user.getUsername() == null ||
                user.getUsername().trim().isEmpty()) {
            return false;
        }

        if (user.getEmail() == null ||
                user.getEmail().trim().isEmpty()) {
            return false;
        }

        if (user.getPasswordHash() == null ||
                user.getPasswordHash().trim().isEmpty()) {
            return false;
        }

        User existingUserByUsername =
                userDAO.getUserByUsername(user.getUsername());

        if (existingUserByUsername != null) {
            return false;
        }

       // User existingUserByEmail =
               // userDAO.getUserByEmail(user.getEmail());

//if (existingUserByEmail != null) {
           // return false;
       // }

        // Default values for a newly registered user
        //user.setStatus("ACTIVE");
       // user.setRole("USER");

        return userDAO.addUser(user);
    }

    @Override
    public User login(String username, String password) {

        // Validate username
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        // Find user by username
        User user = userDAO.getUserByUsername(username);

        // User does not exist
        if (user == null) {
            return null;
        }

        // Check password
        if (!user.getPasswordHash().equals(password)) {
            return null;
        }

        // Check whether user is active
        if (!"ACTIVE".equals(user.getStatus())) {
            return null;
        }

        // Login successful
        return user;
    }

    @Override
    public User getUserById(int userId) {
        // TODO: Implement get user by ID
        return userDAO.getUserById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        // TODO: Implement get user by username
        return userDAO.getUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        // TODO: Implement get all users
        return userDAO.getAllUsers();
    }

    @Override
    public boolean updateUser(User user) {
        // TODO: Add validation
        return userDAO.updateUser(user);
    }

    @Override
    public boolean deactivateUser(int userId) {
        // TODO: Implement deactivation
        return false;
    }

    @Override
    public boolean deleteuser(int userID) {
        return userDAO.deleteuser(userID);

    }


}