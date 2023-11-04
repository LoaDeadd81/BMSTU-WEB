CREATE TABLE if not exists "user"
(
    id       int generated always as identity primary key,
    login    text not null,
    password text not null,
    is_admin bool DEFAULT false
);

-- CREATE TYPE recipe_state AS ENUM ('CLOSE', 'READY_TO_PUBLISH', 'PUBLISHED');

create table if not exists recipe
(
    id           int generated always as identity primary key,
    name        text      not null,
    description text      not null,
    time        int       not null check ( time > 0 ),
    servings_num int check ( servings_num > 0 ),
    protein      int check ( protein >= 0 ),
    fat          int check ( fat >= 0 ),
    carbon       int check ( carbon >= 0 ),
    date        timestamp not null,
    state       int check ( state > 0 and state <= 3) default 0,
    ownerid      int,
    constraint fk_owner foreign key (ownerid) references "user" (id) on delete cascade
);

create table if not exists comment
(
    id       int generated always as identity primary key,
    date     timestamp not null,
    text     text      not null,
    autorid  int,
    recipeid int,
    constraint fk_autor foreign key (autorid) references "user" (id) on delete cascade,
    constraint fk_recipe foreign key (recipeid) references recipe (id) on delete cascade
);

create table if not exists stage
(
    id          int generated always as identity primary key,
    time      int check ( time > 0 ),
    description text not null,
    order_num int not null check ( order_num > 0 ),
    recipeid    int,
    constraint fk_recipe foreign key (recipeid) references recipe (id) on delete cascade,
    unique (recipeid, order_num)
);

-- CREATE TYPE ing_type AS ENUM ('MEAT', 'HORTICULTURAL_PROD', 'TASTE_PROD', 'FATS', 'MILK_PROD', 'CONFECTIONERY', 'GRAIN', 'FISH', 'EGGS');

create table if not exists ingredient
(
    id                int generated always as identity primary key,
    name              text     not null,
    type int not null check ( type > 0 and type <= 9),
    nutritional_value int check ( nutritional_value > 0)
);

create table if not exists saved_recipes
(
    id       int generated always as identity primary key,
    userid   int,
    recipeid int,
    constraint fk_autor foreign key (userid) references "user" (id) on delete cascade,
    constraint fk_recipe foreign key (recipeid) references recipe (id) on delete cascade
);

-- CREATE TYPE proc_type AS ENUM ('WASH', 'DRY', 'CLEAR', 'CUT', 'MIX', 'BOIL', 'FRY', 'BAKE', 'PUT_OUT');

create table if not exists ingredient_list
(
    id              int generated always as identity primary key,
    amount          int not null check ( amount > 0 ),
    processing_type integer check ( processing_type > 0 and processing_type <= 9),
    ingredientid    int,
    stageid         int,
    constraint fk_ingredient foreign key (ingredientid) references ingredient (id) on delete cascade,
    constraint fk_stage foreign key (stageid) references stage (id) on delete cascade
);

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

CREATE ROLE main_role;
GRANT CONNECT ON DATABASE back TO main_role;
GRANT ALL ON SCHEMA public TO main_role;
GRANT ALL ON ALL TABLES IN SCHEMA public TO main_role;
CREATE USER main_user WITH PASSWORD 'main_user';
GRANT main_role TO main_user;

CREATE ROLE read_only_role;
GRANT CONNECT ON DATABASE back TO read_only_role;
GRANT USAGE ON SCHEMA public TO read_only_role;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO read_only_role;
CREATE USER read_user WITH PASSWORD 'read_user';
GRANT read_only_role TO read_user;