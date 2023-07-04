CREATE TABLE IF NOT EXISTS auto_post
(
    id              serial                                  PRIMARY KEY,
    description     varchar                                 NOT NULL,
    created         timestamp                               NOT NULL,
    auto_user_id    int         REFERENCES auto_user(id)    NOT NULL
);