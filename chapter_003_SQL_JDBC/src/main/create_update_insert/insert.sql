--  Категории заявки.
INSERT INTO item_categories(name) 
VALUES('Высшая категория'),
('Средняя категория'),
('Без категории');

--Состояние заявки.
INSERT INTO item_states(name)
VALUES('Выполнена'),
('В процессе'),
('Отменена'),
('Провалена');

--Роли.
INSERT INTO roles(name)
VALUES('Admin'),
('SuperAdmin'),
('User'),
('Nulity');

--Права ролей.
INSERT INTO role_rights(name)
VALUES('Чтение'),
('Запись'),
('Удаление'),
('Создание');

--many to many для role - role_rights
INSERT INTO roles_with_role_rights(role_id, role_rights_id)
VALUES(1, 1),
(1, 2),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(3, 1);

--Пользователи
INSERT INTO users(name, surname, role_id)
VALUES('Иван', 'Скворчук', 1),
('Матвей', 'Ярков', 4),
('Настя', 'Чугунова', 2);

--Заявки.
INSERT INTO items(item_state_id, item_type_id, user_id)
VALUES(1, 1, 1),
(2, 1, 2),
(3, 3, 3);


--Комментарии Заявок.
INSERT INTO item_comments(description, item_id)
VALUES ('Заявка была так себе', 1),
('Лучшая заявка в мире', 2),
('Без поллитра не обойтись', 3);

-- Приложенные Файлы.
INSERT INTO attachments(item_id, name)
VALUES(1, 'AppleBreak.jar');
INSERT INTO attachments(description, item_id, name)
VALUES('Утилитка', 2, 'USACrack.class');