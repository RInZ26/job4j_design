--1. Вывести список всех машин и все привязанные к ним детали.
SELECT c.name, b.name, e.name, t.name FROM car AS c
INNER JOIN body AS b ON c.body_id = b.id
INNER JOIN engine AS e ON c.engine_id = e.id
INNER JOIN transmission AS t ON c.transmission_id = t.id

--2. Вывести отдельно детали, которые не используются в машине, кузова, двигатели, коробки передач.
SELECT b.name AS bodyName FROM body AS b
LEFT JOIN car AS c ON c.body_id = b.id
WHERE c.id IS NULL

SELECT e.name AS engineName FROM car AS c
RIGHT JOIN engine AS e ON c.engine_id = e.id
WHERE c.id IS NULL

SELECT t.name AS transmissionName FROM transmission AS t
LEFT JOIN car AS c ON c.transmission_id = t.id
WHERE c.id IS NULL
