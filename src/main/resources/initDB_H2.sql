DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS dishes CASCADE;
DROP TABLE IF EXISTS restaurants CASCADE;
DROP TABLE IF EXISTS votes CASCADE;
DROP SEQUENCE IF EXISTS global_seq;
CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id integer default next value for GLOBAL_SEQ not null primary key,
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    CONSTRAINT user_unique_constraint UNIQUE (email)
);
CREATE INDEX email_password_idx ON users (email, password);

CREATE TABLE user_roles
(
    user_id          INTEGER                           NOT NULL,
    role             VARCHAR                           NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
    id integer default next value for GLOBAL_SEQ not null primary key,
    name             VARCHAR                           NOT NULL,
    enable           BOOLEAN    DEFAULT TRUE           NOT NULL
);

CREATE TABLE dishes
(
    restaurant_id    INTEGER                           NOT NULL,
    name             VARCHAR                           NOT NULL,
    cost             DOUBLE                            NOT NULL,
    date             DATE DEFAULT CURRENT_DATE         NOT NULL,
    CONSTRAINT DISHES_RESTAURANTS_ID_fk UNIQUE (restaurant_id, name, date),
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS(id) ON DELETE CASCADE
);

CREATE TABLE votes
(
    id integer default next value for GLOBAL_SEQ not null primary key,
    restaurant_id   INTEGER                            NOT NULL,
    user_id         INTEGER                            NOT NULL,
    date_time       TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id)       REFERENCES USERS(id)       ON DELETE CASCADE
);
CREATE INDEX restaurant_id_date_time ON VOTES (restaurant_id, date_time);