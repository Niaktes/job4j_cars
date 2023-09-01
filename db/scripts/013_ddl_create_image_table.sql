CREATE TABLE image (
    id          serial  PRIMARY KEY,
    name        text    NOT NULL,
    path        text    NOT NULL,
    post_id     int     NOT NULL    REFERENCES post(id)
);