--liquibase formatted sql

-- changeset yourname:001_create_tables

create table if not exists categories (
    id serial primary key,
    name varchar(255) not null
);

create table if not exists tags (
    id serial primary key,
    name varchar(255) not null
);

create table if not exists pets (
    id serial primary key,
    name varchar(255) not null,
    category_id int,
    status varchar(255),
    foreign key (category_id) references categories(id) on delete cascade
);

create table if not exists pets_tags (
    pet_id int,
    tag_id int,
    primary key (pet_id, tag_id),
    foreign key (pet_id) references pets(id) on delete cascade,
    foreign key (tag_id) references tags(id) on delete cascade
);

-- rollback drop table if exists pets_tags, tags, pets, categories;
