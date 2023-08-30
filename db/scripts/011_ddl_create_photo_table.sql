CREATE TABLE IF NOT EXISTS photo (
    id          serial      PRIMARY KEY,
    name        varchar     NOT NULL,
    path        varchar     NOT NULL UNIQUE,
    post_id     int         REFERENCES auto_post(id)
);