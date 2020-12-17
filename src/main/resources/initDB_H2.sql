DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS restaurants;
DROP SEQUENCE IF EXISTS global_seq;
CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id integer default next value for GLOBAL_SEQ not null primary key,
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL
);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
    id integer default next value for GLOBAL_SEQ not null primary key,
    name             VARCHAR                           NOT NULL,
--     date             DATE DEFAULT CURRENT_DATE         NOT NULL,
);

CREATE TABLE dishes
(
    restaurant_id    INTEGER                           ,
    name             VARCHAR                           NOT NULL,
    cost             DOUBLE                            NOT NULL,
    date             DATE DEFAULT CURRENT_DATE         /*NOT NULL*/,
    /*CONSTRAINT DISHES_RESTAURANTS_ID_fk UNIQUE (restaurant_id,name),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
                                          ON DELETE CASCADE*/
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS(id) ON DELETE CASCADE
);