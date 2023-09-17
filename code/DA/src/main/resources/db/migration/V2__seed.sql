insert into "user" (login, password, is_admin)
values ('login1', 'password1', true),
       ('login2', 'password2', false),
       ('login3', 'password3', false),
       ('login4', 'password4', false),
       ('login5', 'password5', false);

insert into recipe (name, description, time, servings_num, protein, fat, carbon, date, published, ownerid)
VALUES ('name1', 'desc1', 1, 1, 1, 1, 1, '2023-01-01', true, 1),
       ('name2', 'desc2', 2, 2, 2, 2, 2, '2023-02-02', false, 2),
       ('name3', 'desc3', 3, 3, 3, 3, 3, '2023-03-03', false, 3),
       ('name4', 'desc4', 4, 4, 4, 4, 4, '2023-04-04', false, 4),
       ('name5', 'desc5', 5, 5, 5, 5, 5, '2023-05-05', false, 5);

insert into comment (date, text, autorid, recipeid)
VALUES ('2023-01-01', 'text1', 1, 1),
       ('2023-02-02', 'text2', 2, 2),
       ('2023-03-03', 'text3', 3, 3),
       ('2023-04-04', 'text4', 4, 4),
       ('2023-05-05', 'text5', 5, 5);

insert into stage (time, description, recipeid)
values (1, 'desc1', 1),
       (2, 'desc2', 2),
       (3, 'desc3', 3),
       (4, 'desc4', 4),
       (5, 'desc5', 5);

insert into ingredient (name, type, nutritional_value)
VALUES ('name1', 'MEAT', 1),
       ('name2', 'MEAT', 2),
       ('name3', 'MEAT', 3),
       ('name4', 'MEAT', 4),
       ('name5', 'MEAT', 5);

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
VALUES (1, 'WASH', 1, 1),
       (1, 'DRY', 2, 2),
       (1, 'CLEAR', 3, 3),
       (1, 'CUT', 4, 4),
       (1, 'MIX', 5, 5),

       (2, 'WASH', 2, 2),
       (2, 'DRY', 3, 3),
       (2, 'CLEAR', 4, 4),
       (2, 'CUT', 5, 5),

       (3, 'WASH', 3, 3),
       (3, 'DRY', 4, 4),
       (3, 'CLEAR', 5, 5),

       (4, 'WASH', 4, 4),
       (4, 'DRY', 5, 5),

       (5, 'WASH', 5, 5);

insert into publish_queue (recipeid)
VALUES (4);
--
-- INSERT INTO ingredient ("name", nutritional_value, "type")
-- VALUES ('name6', 789, 'MEAT');
