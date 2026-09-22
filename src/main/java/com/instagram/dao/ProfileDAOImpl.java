package com.instagram.dao;

import com.instagram.model.Profile;
import com.instagram.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileDAOImpl implements ProfileDAO {

    @Override
    public boolean addProfile(Profile profile) {

        String sql = "INSERT INTO profiles " +
                "(user_id, full_name, bio, phone, profile_image_url) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, profile.getUserId());
            statement.setString(2, profile.getFullName());
            statement.setString(3, profile.getBio());
            statement.setString(4, profile.getPhone());
            statement.setString(5, profile.getProfileImageUrl());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Profile getProfileById(int profileId) {

        String sql = "SELECT * FROM profiles WHERE profile_id = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, profileId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Profile profile = new Profile();

                    profile.setProfileId(
                            resultSet.getInt("profile_id"));

                    profile.setUserId(
                            resultSet.getInt("user_id"));

                    profile.setFullName(
                            resultSet.getString("full_name"));

                    profile.setBio(
                            resultSet.getString("bio"));

                    profile.setPhone(
                            resultSet.getString("phone"));

                    profile.setProfileImageUrl(
                            resultSet.getString("profile_image_url"));

                    return profile;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Profile getProfileByUserId(int userId) {

        String sql = "SELECT * FROM profiles WHERE user_id = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Profile profile = new Profile();

                    profile.setProfileId(
                            resultSet.getInt("profile_id"));

                    profile.setUserId(
                            resultSet.getInt("user_id"));

                    profile.setFullName(
                            resultSet.getString("full_name"));

                    profile.setBio(
                            resultSet.getString("bio"));

                    profile.setPhone(
                            resultSet.getString("phone"));

                    profile.setProfileImageUrl(
                            resultSet.getString("profile_image_url"));

                    return profile;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean updateProfile(Profile profile) {

        String sql = "UPDATE profiles SET " +
                "full_name = ?, " +
                "bio = ?, " +
                "phone = ?, " +
                "profile_image_url = ? " +
                "WHERE profile_id = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, profile.getFullName());
            statement.setString(2, profile.getBio());
            statement.setString(3, profile.getPhone());
            statement.setString(4, profile.getProfileImageUrl());
            statement.setInt(5, profile.getProfileId());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProfile(int profileId) {
        // TODO: Implement DELETE query
        return false;
    }
}