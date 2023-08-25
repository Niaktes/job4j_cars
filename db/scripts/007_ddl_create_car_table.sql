CREATE TABLE IF NOT EXISTS car (
    id          serial      PRIMARY KEY,
    name        varchar     NOT NULL,
    engine_id   int         NOT NULL    REFERENCES engine(id)
);