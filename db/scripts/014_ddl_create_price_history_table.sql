CREATE TABLE price_history (
    id          serial      PRIMARY KEY,
    price       bigint      NOT NULL,
    date        timestamp   NOT NULL,
    post_id     int         REFERENCES post(id) ON DELETE CASCADE
);