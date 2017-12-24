CREATE TABLE users (
    username    varchar(40) CONSTRAINT firstkey PRIMARY KEY,
	password    varchar(40) NOT NULL,
	privileges  varchar(40) NOT NULL
);