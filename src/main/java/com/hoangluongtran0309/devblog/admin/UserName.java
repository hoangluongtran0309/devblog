package com.hoangluongtran0309.devblog.admin;

import java.util.Objects;

import org.springframework.util.Assert;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserName {

    private String firstName;
    private String lastName;

    protected UserName() {

    }

    public UserName(String firstName, String lastName) {
        Assert.hasText(firstName, "First name cannot be blank");
        Assert.hasText(lastName, "Last name cannot be blank");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.firstName);
        hash = 67 * hash + Objects.hashCode(this.lastName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserName other = (UserName) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        return Objects.equals(this.lastName, other.lastName);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .toString();
    }
}