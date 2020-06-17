--create database i_love_music;
--Роли.
CREATE TABLE roles(
	id SERIAL PRIMARY KEY,
	name VARCHAR NOT NULL
);

--Права ролей.
CREATE TABLE role_rights(
	id SERIAL PRIMARY KEY,
	name VARCHAR(40)
);

--many to many для role - role_rights
CREATE TABLE roles_with_role_rights(
	id SERIAL PRIMARY KEY,
	role_id INT REFERENCES roles(id),
	role_rights_id INT REFERENCES role_rights(id)
);

--Состояние заявки.
CREATE TABLE item_states(
	id SERIAL PRIMARY KEY,
	name VARCHAR(40)
);
--  Категории заявки.
CREATE TABLE item_categories(
	id SERIAL PRIMARY KEY,
	name VARCHAR(40)
);
--Пользователи
CREATE TABLE users(
	id SERIAL PRIMARY KEY,
	name VARCHAR(15) NOT NULL,
	surname VARCHAR (15) NOT NULL,
	role_id INT REFERENCES roles(id)
);
--Заявки.
CREATE TABLE items(
	id SERIAL PRIMARY KEY,
	user_id INT REFERENCES users(id),
	item_type_id INT REFERENCES item_categories(id),
	item_state_id INT REFERENCES item_states(id)
);
-- Приложенные Файлы.
CREATE TABLE attachments(
	id SERIAL PRIMARY KEY,
	name VARCHAR(40) NOT NULL,
	description VARCHAR(100) NOT NULL DEFAULT 'I WAS SO LAZY FOR IT',
	item_id INT REFERENCES items(id)
);
--Комментарии Заявок.
CREATE TABLE item_comments(
	id SERIAL PRIMARY KEY,
	description TEXT,
	item_id INT REFERENCES items(id)
);