package com.instagram.dao;

import com.instagram.model.User;
import com.instagram.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean addUser(User users) {

        String sql = "INSERT INTO users " +
                "(username, email, password_hash, status, role) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, users.getUsername());
            statement.setString(2, users.getEmail());
            statement.setString(3, users.getPasswordHash());
            statement.setString(4, users.getStatus());
            statement.setString(5, users.getRole());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User users = new User();

                users.setUserId(resultSet.getInt("user_id"));
                users.setUsername(resultSet.getString("username"));
                users.setEmail(resultSet.getString("email"));
                users.setPasswordHash(resultSet.getString("password_hash"));

                return users;

            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

       // get user by ID
    @Override
    public User getUserById(int userId) {
        // TODO: Implement SELECT query by ID
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setUserId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setStatus(resultSet.getString("status"));
                user.setRole(resultSet.getString("role"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User getUserByUsername(String username) {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setUserId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setStatus(resultSet.getString("status"));
                user.setRole(resultSet.getString("role"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

  //  @Override
//    public User getUserByEmail(String email) {
//
//        String sql = "SELECT * FROM users WHERE email = ?";
//
//        try (Connection connection = JDBCUtil.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setString(1, email);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//
//                User user = new User();
//
//                user.setUserId(resultSet.getInt("user_id"));
//                user.setUsername(resultSet.getString("username"));
//                user.setEmail(resultSet.getString("email"));
//                user.setPasswordHash(resultSet.getString("password_hash"));
//                user.setStatus(resultSet.getString("status"));
//                user.setRole(resultSet.getString("role"));
//
//                return user;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    @Override
    public List<User> getAllUsers() {
        // TODO: Implement SELECT ALL query
        List<User> users = new java.util.ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                User user = new User();

                user.setUserId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setStatus(resultSet.getString("status"));
                user.setRole(resultSet.getString("role"));

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public boolean updateUser(User user) {
        // TODO: Implement UPDATE query
        String sql = "UPDATE users SET username = ?, email = ?, " +
                "password_hash = ?, status = ?, role = ? " +
                "WHERE user_id = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getStatus());
            statement.setString(5, user.getRole());
            statement.setInt(6, user.getUserId());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Unable to update user: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean deleteuser(int userId) {
        // TODO: Implement DELETE query
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Unable to delete user: " + e.getMessage());
            return false;
        }
    }
}