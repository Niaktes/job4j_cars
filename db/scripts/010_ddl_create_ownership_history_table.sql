CREATE TABLE IF NOT EXISTS ownership_history (
    id          serial      PRIMARY KEY,
    startAt     timestamp   NOT NULL,
    endAt       timestamp,
    car_id      int         NOT NULL    REFERENCES car(id),
    owner_id    int         NOT NULL    REFERENCES owner(id)
);