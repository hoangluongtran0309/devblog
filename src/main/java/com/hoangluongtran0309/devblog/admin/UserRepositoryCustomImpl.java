package com.hoangluongtran0309.devblog.admin;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Override
    public UserId nextId() {
        return UserId.generate();
    }
}
