INSERT INTO users(name)
VALUES ('Настя'),
('Катя'),
('Матвей');

INSERT INTO meetings(name)
VALUES('Встреча века'),
('Встреча недели'),
('Никому ненужная встреча');

INSERT INTO statuses(name)
VALUES('approved'),
('declined');

INSERT INTO meeting_with_user
VALUES(1, 1, 1),
(1, 2, 1),
(2,1,0),
(3, 1, 0),
(2,2,1);