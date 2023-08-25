ALTER TABLE auto_post ADD COLUMN
    car_id      int     NOT NULL    REFERENCES car(id);