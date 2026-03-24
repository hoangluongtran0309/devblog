package com.hoangluongtran0309.devblog.media;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.hoangluongtran0309.devblog.infrastructure.persistence.JpaConfiguration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ActiveProfiles("data-jpa-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ JpaConfiguration.class })
class MediaRepositoryTest {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private MediaId mediaId;
    private MediaPublicId mediaPublicId;
    private MediaUrl mediaUrl;
    private MediaFileName mediaFileName;
    private MediaType mediaType;

    @BeforeEach
    void setUp() {
        mediaId = MediaId.generate();
        mediaPublicId = new MediaPublicId("public-id-123");
        mediaUrl = new MediaUrl("https://example.com/image.png");
        mediaFileName = new MediaFileName("image.png");
        mediaType = MediaType.IMAGE;

        assertThat(mediaRepository.count()).isZero();
    }

    private Media createMedia() {
        return new Media(
                mediaId,
                mediaPublicId,
                mediaUrl,
                mediaFileName,
                mediaType,
                1024L);
    }

    @Test
    void testSaveMedia() {
        Media media = createMedia();

        mediaRepository.save(media);
        entityManager.flush();

        assertThat(jdbcTemplate.queryForObject("SELECT id FROM db_media", UUID.class))
                .isEqualTo(mediaId.asString());
        assertThat(jdbcTemplate.queryForObject("SELECT media_public_id FROM db_media", String.class))
                .isEqualTo(mediaPublicId.asString());
    }

    @Test
    void testFindById() {
        Media media = createMedia();

        mediaRepository.save(media);
        entityManager.flush();
        entityManager.clear();

        assertThat(mediaRepository.findById(mediaId)).isPresent();
    }

    @Test
    void testFindByMediaType() {
        Media media = createMedia();

        mediaRepository.save(media);
        entityManager.flush();
        entityManager.clear();

        var page = mediaRepository.findByMediaType(
                MediaType.IMAGE,
                PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testDeleteMedia() {
        Media media = createMedia();

        mediaRepository.save(media);
        entityManager.flush();

        mediaRepository.deleteById(mediaId);
        entityManager.flush();
        entityManager.clear();

        assertThat(mediaRepository.findById(mediaId)).isEmpty();
    }
}