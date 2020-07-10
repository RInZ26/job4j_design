CREATE TABLE meetings(
id SERIAL PRIMARY KEY,
name VARCHAR(100) 
);

CREATE TABLE users(
id SERIAL PRIMARY KEY,
name VARCHAR(100)
);

CREATE TABLE statuses(
id SERIAL PRIMARY KEY,
name VARCHAR (100) NOT NULL UNIQUE
);

CREATE TABLE meeting_with_user(
user_id int REFERENCES users(id) NOT NULL,
meeting_id int NOT NULL,
status_id int,
CONSTRAINT pk_meeting_with_user PRIMARY KEY (user_id, meeting_id)
);