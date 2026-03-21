CREATE TABLE db_category (
    id UUID NOT NULL,
    category_name VARCHAR NOT NULL,
    category_icon VARCHAR,
    category_slug VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE db_category
ADD CONSTRAINT UK_category_slug UNIQUE (category_slug);
CREATE TABLE db_tag (
    id UUID NOT NULL,
    tag_name VARCHAR NOT NULL,
    tag_slug VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE db_tag
ADD CONSTRAINT UK_tag_slug UNIQUE (tag_slug);
CREATE TABLE db_article (
    id UUID NOT NULL,
    article_title VARCHAR NOT NULL,
    article_summary VARCHAR NOT NULL,
    article_content TEXT NOT NULL,
    image_preview VARCHAR NOT NULL,
    article_slug VARCHAR NOT NULL,
    article_status VARCHAR,
    published_at TIMESTAMP,
    category_id UUID,
    version BIGINT DEFAULT 0 NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE db_article
ADD CONSTRAINT UK_article_slug UNIQUE (article_slug);
ALTER TABLE db_article
ADD CONSTRAINT FK_article_category FOREIGN KEY (category_id) REFERENCES db_category(id);
CREATE TABLE article_tag (
    article_id UUID NOT NULL,
    tag_id UUID NOT NULL
);
ALTER TABLE article_tag
ADD CONSTRAINT FK_article_tag_article FOREIGN KEY (article_id) REFERENCES db_article(id);
ALTER TABLE article_tag
ADD CONSTRAINT FK_article_tag_tag FOREIGN KEY (tag_id) REFERENCES db_tag(id);
CREATE TABLE db_project (
    id UUID NOT NULL,
    project_name VARCHAR NOT NULL,
    project_summary VARCHAR NOT NULL,
    project_content TEXT NOT NULL,
    image_preview VARCHAR NOT NULL,
    project_slug VARCHAR NOT NULL,
    project_status VARCHAR,
    published_at TIMESTAMP,
    version BIGINT DEFAULT 0 NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE db_project
ADD CONSTRAINT UK_project_slug UNIQUE (project_slug);
CREATE TABLE project_tag (
    project_id UUID NOT NULL,
    tag_id UUID NOT NULL
);
ALTER TABLE project_tag
ADD CONSTRAINT FK_project_tag_project FOREIGN KEY (project_id) REFERENCES db_project(id);
ALTER TABLE project_tag
ADD CONSTRAINT FK_project_tag_tag FOREIGN KEY (tag_id) REFERENCES db_tag(id);