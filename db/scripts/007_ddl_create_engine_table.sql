CREATE TABLE engine (
    id              serial  PRIMARY KEY,
    fuel_type_id    int     NOT NULL        REFERENCES fuel_type(id),
    engine_size_id  int     NOT NULL        REFERENCES engine_size(id)
);