-- кузов
CREATE TABLE body (
id SERIAL PRIMARY KEY,
name VARCHAR(20)
);
--двигатель
CREATE TABLE engine (
id SERIAL PRIMARY KEY,
name VARCHAR(20)
);
--коробка передач
CREATE TABLE transmission (
id SERIAL PRIMARY KEY,
name VARCHAR(30)
);
--машина
CREATE TABLE car (
id SERIAL PRIMARY KEY,
name TEXT,
body_id INT REFERENCES body(id),
engine_id INT REFERENCES engine(id),
transmission_id INT REFERENCES transmission(id)
);
