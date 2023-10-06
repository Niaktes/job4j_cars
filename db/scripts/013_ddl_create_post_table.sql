CREATE TABLE post (
    id              serial      PRIMARY KEY,
    description     text        NOT NULL,
    car_id          int         NOT NULL        UNIQUE REFERENCES car(id),
    created         timestamp   NOT NULL        DEFAULT now(),
    sold            boolean     NOT NULL        DEFAULT FALSE,
    price           bigint      NOT NULL,
    user_id         int         NOT NULL        REFERENCES auto_user(id),
    image_id        int                         REFERENCES image(id),
    UNIQUE (car_id, user_id)
);