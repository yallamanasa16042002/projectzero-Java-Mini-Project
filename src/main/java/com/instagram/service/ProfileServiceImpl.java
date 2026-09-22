package com.instagram.service;

import com.instagram.dao.ProfileDAO;
import com.instagram.dao.ProfileDAOImpl;
import com.instagram.model.Profile;

public class ProfileServiceImpl implements ProfileService {

    private ProfileDAO profileDAO;

    public ProfileServiceImpl() {
        this.profileDAO = new ProfileDAOImpl();
    }
    @Override
    public boolean createProfile(Profile profile) {

        if (profile == null) {
            return false;
        }

        if (profile.getUserId() <= 0) {
            return false;
        }

        Profile existingProfile =
                profileDAO.getProfileByUserId(profile.getUserId());

        if (existingProfile != null) {
            return false;
        }

        return profileDAO.addProfile(profile);
    }

    @Override
    public Profile getProfileById(int profileId) {

        if (profileId <= 0) {
            return null;
        }

        return profileDAO.getProfileById(profileId);
    }

    @Override
    public Profile getProfileByUserId(int userId) {

        if (userId <= 0) {
            return null;
        }

        return profileDAO.getProfileByUserId(userId);
    }

    @Override
    public boolean updateProfile(Profile profile) {

        // 1. Check whether profile object is null
        if (profile == null) {
            return false;
        }

        // 2. Check whether profileId is valid
        if (profile.getProfileId() <= 0) {
            return false;
        }

        // 3. Check whether profile exists
        Profile existingProfile =
                profileDAO.getProfileById(profile.getProfileId());

        if (existingProfile == null) {
            return false;
        }

        // 4. Call DAO to update profile
        return profileDAO.updateProfile(profile);
    }

    @Override
    public boolean deleteProfile(int profileId) {
        // TODO: Implement DELETE query
        return false;
    }
}