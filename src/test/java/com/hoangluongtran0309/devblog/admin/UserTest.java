package com.hoangluongtran0309.devblog.admin;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void shouldCreateAdministratorWithAdminRole() {
        UserId id = UserId.generate();
        UserName userName = new UserName("Luong", "Tran");
        Email email = new Email("admin@test.com");

        User user = User.createAdministrator(id, userName, email, "12345678");

        assertThat(user.getUserName()).isEqualTo(userName);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getRoles()).containsExactly(UserRole.ADMIN);
    }

    @Test
    void shouldChangePassword() {
        UserId id = UserId.generate();
        UserName userName = new UserName("Luong", "Tran");
        Email email = new Email("admin@test.com");

        User user = User.createAdministrator(id, userName, email, "12345678");

        user.changePassword("123456789");

        assertThat(user.getPassword()).isEqualTo("123456789");
    }

    @Test
    void administratorShouldHaveOnlyAdminRole() {
        UserId id = UserId.generate();
        UserName userName = new UserName("Luong", "Tran");
        Email email = new Email("admin@test.com");

        User user = User.createAdministrator(id, userName, email, "12345678");

        assertThat(user.getRoles())
                .hasSize(1)
                .contains(UserRole.ADMIN);
    }
}
