package com.hoangluongtran0309.devblog.admin;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.hoangluongtran0309.devblog.infrastructure.persistence.JpaConfiguration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ActiveProfiles("data-jpa-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ JpaConfiguration.class })
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private UserId userId;
    private UserName userName;
    private Email email;

    @BeforeEach
    void setUp() {
        userId = UserId.generate();
        userName = new UserName("Admin", "Test");
        email = new Email("admin@test.com");

        assertThat(userRepository.count()).isZero();
    }

    private User createAdministrator() {
        return new User(
                userId,
                userName,
                email,
                "hashPassword",
                Set.of(UserRole.ADMIN));
    }

    @Test
    void testSaveUser() {
        User admin = createAdministrator();

        userRepository.save(admin);
        entityManager.flush();

        assertThat(jdbcTemplate.queryForObject("SELECT id FROM db_user", UUID.class))
                .isEqualTo(userId.asString());

        assertThat(jdbcTemplate.queryForObject("SELECT email FROM db_user", String.class))
                .isEqualTo(email.asString());
    }

    @Test
    void testExistsByEmail() {
        User admin = createAdministrator();

        userRepository.save(admin);
        entityManager.flush();
        entityManager.clear();

        assertThat(userRepository.existsByEmail(email)).isTrue();
    }

    @Test
    void testFindByEmail() {
        User admin = createAdministrator();

        userRepository.save(admin);
        entityManager.flush();
        entityManager.clear();

        assertThat(userRepository.findByEmail(email)).isPresent();
    }
}
