package com.hoangluongtran0309.devblog.admin;

public class ChangePasswordParameters {

    private final String currentPassword;
    private final String newPassword;
    private final String confirmPassword;

    public ChangePasswordParameters(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
