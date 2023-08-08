CREATE TABLE IF NOT EXISTS participates (
    id              serial  PRIMARY KEY,
    auto_post_id    int     NOT NULL REFERENCES auto_post(id),
    auto_user_id    int     NOT NULL REFERENCES auto_user(id),
    UNIQUE (auto_post_id, auto_user_id)
)