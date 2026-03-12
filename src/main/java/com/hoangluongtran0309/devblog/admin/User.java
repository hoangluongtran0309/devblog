package com.hoangluongtran0309.devblog.admin;

import java.util.Set;

import com.hoangluongtran0309.devblog.infrastructure.persistence.BaseEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "db_user")
public class User extends BaseEntity<UserId> {

    @Embedded
    @NotNull
    private UserName userName;

    @Embedded
    @NotNull
    private Email email;

    @NotNull
    private String password;

    @ElementCollection(targetClass = UserRole.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles")
    @Column(name = "role")
    private Set<UserRole> roles;

    protected User() {

    }

    public User(UserId id, UserName userName, Email email, String password, Set<UserRole> roles) {
        super(id);
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserName getUserName() {
        return userName;
    }

    public Email getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }
}
