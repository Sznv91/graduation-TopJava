DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS votes;
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
    enable           BOOLEAN    DEFAULT TRUE           NOT NULL
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

CREATE TABLE votes
(
    id INTEGER DEFAULT NEXT VALUE FOR GLOBAL_SEQ NOT NULL PRIMARY KEY,
    date_time       TIMESTAMP                           ,
    user_id         INTEGER                             ,
    restaurant_id   INTEGER                             ,
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE,
    CONSTRAINT restaurant_id_fk FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS (id) on DELETE CASCADE
);