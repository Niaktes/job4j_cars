ALTER TABLE car ADD COLUMN
    model_id    int     NOT NULL    REFERENCES model(id);