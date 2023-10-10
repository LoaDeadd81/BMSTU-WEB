insert into "user" (login, password, is_admin)
values ('login1', 'password1', true),
       ('login2', 'password2', false),
       ('login3', 'password3', false),
       ('login4', 'password4', false),
       ('login5', 'password5', false);

insert into recipe (name, description, time, servings_num, protein, fat, carbon, date, state, ownerid)
VALUES ('name1', 'desc1', 1, 1, 1, 1, 1, '2023-01-01', 3, 1),
       ('name2', 'desc2', 2, 2, 2, 2, 2, '2023-02-02', 2, 2),
       ('name3', 'desc3', 3, 3, 3, 3, 3, '2023-03-03', 1, 3),
       ('name4', 'desc4', 4, 4, 4, 4, 4, '2023-04-04', 1, 4),
       ('name5', 'desc5', 5, 5, 5, 5, 5, '2023-05-05', 1, 5);

insert into comment (date, text, autorid, recipeid)
VALUES ('2023-01-01', 'text1', 1, 1),
       ('2023-02-02', 'text2', 2, 2),
       ('2023-03-03', 'text3', 3, 3),
       ('2023-04-04', 'text4', 4, 4),
       ('2023-05-05', 'text5', 5, 5);

insert into stage (time, description, order_num, recipeid)
values (1, 'desc1', 1, 1),
       (2, 'desc2', 1, 2),
       (3, 'desc3', 1, 3),
       (4, 'desc4', 1, 4),
       (5, 'desc5', 1, 5);

insert into ingredient (name, type, nutritional_value)
VALUES ('name1', 1, 1),
       ('name2', 1, 2),
       ('name3', 1, 3),
       ('name4', 1, 4),
       ('name5', 1, 5);

insert into saved_recipes (userID, recipeid)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (3, 3),
       (3, 4),
       (3, 5),
       (4, 4),
       (4, 5),
       (5, 5);

insert into ingredient_list (amount, processing_type, ingredientid, stageid)
VALUES (1, 1, 1, 1),
       (1, 2, 2, 2),
       (1, 3, 3, 3),
       (1, 4, 4, 4),
       (1, 5, 5, 5),

       (2, 1, 2, 2),
       (2, 2, 3, 3),
       (2, 3, 4, 4),
       (2, 4, 5, 5),

       (3, 1, 3, 3),
       (3, 2, 4, 4),
       (3, 3, 5, 5),

       (4, 1, 4, 4),
       (4, 2, 5, 5),

       (5, 1, 5, 5);
