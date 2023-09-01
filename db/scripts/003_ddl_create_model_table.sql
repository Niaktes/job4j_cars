CREATE TABLE model (
    id          serial  PRIMARY KEY,
    name        text    NOT NULL,
    brand_id    int     NOT NULL    REFERENCES brand(id)
);