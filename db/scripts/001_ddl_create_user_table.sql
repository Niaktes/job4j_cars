CREATE TABLE auto_user (
    id          serial      PRIMARY KEY,
    email       text        NOT NULL UNIQUE,
    password    text        NOT NULL,
    name        text        NOT NULL,
    phone       text        NOT NULL UNIQUE
);