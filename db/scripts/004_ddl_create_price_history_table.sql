CREATE TABLE IF NOT EXISTS price_history (
    id          serial      PRIMARY KEY,
    before      bigint      NOT NULL,
    after       bigint      NOT NULL,
    created     timestamp without time zone     DEFAULT now(),
    post_id     int         REFERENCES auto_post(id)    NOT NULL
);