CREATE TABLE db_user (
    id UUID NOT NULL,
    email VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE db_user
ADD CONSTRAINT UK_user_email UNIQUE (email);
CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role VARCHAR NOT NULL
);
ALTER TABLE user_roles
ADD CONSTRAINT FK_user_roles_to_user FOREIGN KEY (user_id) REFERENCES db_user;