CREATE TABLE IF NOT EXISTS owner (
    id          serial      PRIMARY KEY,
    name        varchar     NOT NULL,
    user_id     int         NOT NULL    REFERENCES auto_user(id)
);