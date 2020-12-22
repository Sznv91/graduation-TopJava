DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM DISHES;
DELETE FROM RESTAURANTS;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('ADMIN', 100001);

INSERT INTO RESTAURANTS (NAME/*,DATE*/)
VALUES ('first restaurant'/*, CURRENT_DATE*/),
       ('second restaurant'/*, CURRENT_DATE*/),
       ('Latest restaurant'/*, '2020-10-20'*/);

INSERT INTO DISHES(RESTAURANT_ID, NAME, COST, DATE)
VALUES (100002, 'first dish First restaurant', 1.01, current_date),
       (100002, 'second dish First restaurant', 2.1, current_date),
       (100002, 'Late Dish First restaurant', 3.11, '2020-10-20'),
       (100003, 'first dish Second restaurant', 1.02, current_date),
       (100003, 'second dish Second restaurant', 2.2, current_date),
       (100003, 'Late Dish Second restaurant', 3.22, '2020-10-20'),
       (100004, 'first Dish Latest restaurant', 3.22, '2020-10-20'),
       (100004, 'second Dish Latest restaurant', 3.22, '2020-10-20'),
       (100004, 'Latest Dish Latest restaurant', 3.22, '2020-10-15');

INSERT INTO VOTES (RESTAURANT_ID, USER_ID, DATE_TIME)
VALUES ( 100002, 100001, CURRENT_TIMESTAMP() ),
       ( 100002, 100000, '2020-11-10 09:08:07' );

