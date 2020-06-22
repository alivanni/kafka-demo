CREATE TABLE IF NOT EXISTS metrics (
    id serial PRIMARY KEY,
    type VARCHAR(255),
    value VARCHAR(255),
    timestamp int
);
