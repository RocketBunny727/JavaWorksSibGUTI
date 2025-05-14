--liquibase formatted sql

-- changeset yourname:001_create_tables

create table if not exists users (
    id serial primary key,
    name varchar(255) not null
);

create table if not exists comments (
    id serial primary key,
    content varchar(255) not null,
    authorId int,
    date timestamp not null,
    foreign key (authorId) references users(id) on delete cascade
);

-- rollback drop table if exists users, comments;