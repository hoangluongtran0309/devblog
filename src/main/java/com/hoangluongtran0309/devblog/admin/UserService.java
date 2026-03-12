package com.hoangluongtran0309.devblog.admin;

public interface UserService {

    boolean exists(Email email);

    User getUser(UserId userId);

    User createAdministrator(CreateUserParameters parameters);

    void changePassword(UserId userId, ChangePasswordParameters parameters);
}
