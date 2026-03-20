package com.hoangluongtran0309.devblog.admin;

import java.util.Optional;

public interface UserService {

    boolean existsByEmail(Email email);

    Optional<User> getById(UserId userId);

    User createAdministrator(CreateUserParameters parameters);

    void changePassword(UserId userId, ChangePasswordParameters parameters);
}
