--liquibase formatted sql

-- changeset postgres:001_create_tables

create table catalog (
    id bigserial primary key,
    name varchar(255),
    description text,
    image text,
    parent_catalog_id bigint references catalog(id) on delete cascade
);

create table item (
    id bigserial primary key,
    name varchar(255),
    image text,
    price int,
    description text,
    catalog_id bigint references catalog(id) on delete cascade
);

create table if not exists product (
    id serial primary key,
    name varchar(255) not null,
    image varchar(512) not null,
    price int not null,
    description text
);

-- rollback drop table if exists catalog, product, item;