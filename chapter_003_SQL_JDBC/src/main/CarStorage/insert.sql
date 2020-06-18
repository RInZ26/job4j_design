INSERT INTO body(name)
VALUES('Кузов А'),
('Кузов Б'),
('Кузов С'),
('Бесполезный');

INSERT INTO engine(name)
VALUES('Супер'),
('Мега'),
('Российский'),
('Ненужный');

INSERT INTO transmission(name)
VALUES('Ручник'),
('Автомат'),
('ИлонМаск'),
('Образовательная');

INSERT INTO car(name, body_id, engine_id, transmission_id)
VALUES('Черная молния', 3, 3, 1),
('Tesla', 1, 1, 3),
('WWW', 2, 2, 2);

