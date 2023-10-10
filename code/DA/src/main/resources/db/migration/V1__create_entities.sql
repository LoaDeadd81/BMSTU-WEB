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

