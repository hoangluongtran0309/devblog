CREATE TABLE db_media (
    id UUID NOT NULL,
    media_public_id VARCHAR NOT NULL,
    media_url VARCHAR NOT NULL,
    media_file_name VARCHAR NOT NULL,
    media_type VARCHAR NOT NULL,
    file_size BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE db_media
ADD CONSTRAINT UK_media_public_id UNIQUE (media_public_id);