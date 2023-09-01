CREATE TABLE auto_user (
    id          serial      PRIMARY KEY,
    email       varchar     NOT NULL UNIQUE,
    password    varchar     NOT NULL,
    name        varchar     NOT NULL,
    phone       varchar     NOT NULL UNIQUE
);