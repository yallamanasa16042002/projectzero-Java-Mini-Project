package com.instagram.controller;

import com.instagram.model.Profile;
import com.instagram.service.ProfileService;

public class ProfileController {

    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    public boolean createProfile(Profile profile) {
        return profileService.createProfile(profile);
    }

    public Profile getProfileById(int profileId) {
        return profileService.getProfileById(profileId);
    }

    public Profile getProfileByUserId(int userId) {
        return profileService.getProfileByUserId(userId);
    }

    public boolean updateProfile(Profile profile) {
        return profileService.updateProfile(profile);
    }

    public boolean deleteProfile(int profileId) {
        // TODO: Implement DELETE operation
        return false;
    }
}