create schema feedme;

create table feedme.allergies (user_name varchar(200) not null, ingredient_name varchar(200) not null);
alter  table feedme.allergies add primary key (user_name, ingredient_name);

create table feedme.recipes(
    recipe_name varchar(200) primary key,
    recipe_long_name varchar(512),
    recipe_description varchar(512),
    recipe_cuisine varchar(200)
);

create table feedme.recipe_ingredients(
    recipe_name varchar(200) not null,
    ingredient_name varchar(200) not null,
    ingredient_description varchar(512),
    ingredient_quantity float,
    ingredient_quantity_unit varchar(20)
);
--alter table feedme.recipe_ingredients add primary key(recipe_name, ingredient_name);
alter table feedme.recipe_ingredients add foreign key (recipe_name) references feedme.recipes(recipe_name);

create table feedme.recipe_steps(
    recipe_name varchar(200) not null,
    step_id int not null,
    step_time_minutes int,
    step_description varchar(1012)
);
alter table feedme.recipe_steps add primary key (recipe_name, step_id);
alter table feedme.recipe_steps add foreign key (recipe_name) references feedme.recipes(recipe_name);

create table feedme.recipe_likes(
    user_name varchar(200) not null,
    recipe_name varchar(200) not null,
    like_count int
);
alter table feedme.recipe_likes add primary key (user_name, recipe_name);
alter table feedme.recipe_likes add foreign key (recipe_name) references feedme.recipes(recipe_name);
