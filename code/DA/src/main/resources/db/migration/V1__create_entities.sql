CREATE TABLE if not exists "user"
(
    id       int generated always as identity primary key,
    login    text not null,
    password text not null,
    is_admin bool DEFAULT false
);

create table if not exists recipe
(
    id           int generated always as identity primary key,
    name         text      not null,
    description  text      not null,
    time         int       not null check ( time > 0 ),
    servings_num int check ( servings_num > 0 ),
    protein      int check ( protein >= 0 ),
    fat          int check ( fat >= 0 ),
    carbon       int check ( carbon >= 0 ),
    date         timestamp not null,
    published    bool      not null default false,
    ownerid      int,
    constraint fk_owner foreign key (ownerid) references "user" (id)
);

create table if not exists comment
(
    id       int generated always as identity primary key,
    date     timestamp not null,
    text     text      not null,
    autorid  int,
    recipeid int,
    constraint fk_autor foreign key (autorid) references "user" (id),
    constraint fk_recipe foreign key (recipeid) references recipe (id)
);

create table if not exists stage
(
    id          int generated always as identity primary key,
    time        int  not null check ( time > 0 ),
    description text not null,
    recipeid    int,
    constraint fk_recipe foreign key (recipeid) references recipe (id)
);

CREATE TYPE ing_type AS ENUM ('MEAT', 'HORTICULTURAL_PROD', 'TASTE_PROD', 'FATS', 'MILK_PROD', 'CONFECTIONERY', 'GRAIN', 'FISH', 'EGGS');

create table if not exists ingredient
(
    id                int generated always as identity primary key,
    name              text     not null,
    type              ing_type not null,
    nutritional_value int check ( nutritional_value > 0)
);

create table if not exists saved_recipes
(
    id       int generated always as identity primary key,
    userid   int,
    recipeid int,
    constraint fk_autor foreign key (userid) references "user" (id),
    constraint fk_recipe foreign key (recipeid) references recipe (id)
);

CREATE TYPE proc_type AS ENUM ('WASH', 'DRY', 'CLEAR', 'CUT', 'MIX', 'BOIL', 'FRY', 'BAKE', 'PUT_OUT');

create table if not exists ingredient_list
(
    id              int generated always as identity primary key,
    amount          int not null check ( amount > 0 ),
    processing_type proc_type,
    ingredientid    int,
    stageid         int,
    constraint fk_ingredient foreign key (ingredientid) references ingredient (id),
    constraint fk_stage foreign key (stageid) references stage (id)
);

create table if not exists publish_queue
(
    id       int generated always as identity primary key,
    recipeid int,
    constraint fk_recipe foreign key (recipeid) references recipe (id)
)