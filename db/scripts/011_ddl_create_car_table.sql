CREATE TABLE car (
    id              serial  PRIMARY KEY,
    brand_id        int     NOT NULL        REFERENCES brand(id),
    model_id        int     NOT NULL        REFERENCES model(id),
    body_id         int     NOT NULL        REFERENCES body(id),
    engine_id       int     NOT NULL        REFERENCES engine(id),
    transmission_id int     NOT NULL        REFERENCES transmission(id),
    color_id        int     NOT NULL        REFERENCES color(id),
    category_id     int     NOT NULL        REFERENCES category(id),
    year            int     NOT NULL,
    mileage         int     NOT NULL,
    vin             text    NOT NULL
);