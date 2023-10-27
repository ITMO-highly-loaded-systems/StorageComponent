CREATE SCHEMA IF NOT EXISTS highload;

CREATE TABLE IF NOT EXISTS highload.kvpair (
    id SERIAL PRIMARY KEY,
    Key varchar(35),
    Value varchar(35)
);

INSERT INTO highload.kvpair (key, value) VALUES ('test', 'sjon')

-- DROP TABLE kvpair;