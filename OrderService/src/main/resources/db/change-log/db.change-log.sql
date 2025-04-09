--liquibase formatted sql

-- changeset yourname:001_create_tables

create table if not exists token (
    id serial primary key,
    token varchar(255) not null,
    expiration_date timestamp not null,
    auth_user_id bigint,
    revoked bool not null,
    constraint fk_token_auth_user foreign key (auth_user_id) references auth_user(id) on delete cascade
);

create table if not exists auth_user (
    id serial primary key,
    login varchar(255) not null,
    password varchar(255) not null,
    user_type varchar(255) not null,
    customer_id bigint,
    constraint fk_auth_user_customer foreign key (customer_id) references customer(id) on delete cascade
);

create table if not exists customer (
    id serial primary key,
    name varchar(255) not null,
    address_id bigint unique,
    constraint fk_customer_address foreign key (address_id) references address(id) on delete cascade
);

create table if not exists address (
   id serial primary key,
   city varchar(255) not null,
   street varchar(255) not null,
   number varchar(255) not null,
   zipcode varchar(50) not null
);

create table if not exists orders (
   id serial primary key,
   date timestamp not null,
   status varchar(255) not null,
   customer_id bigint,
   constraint fk_order_customer foreign key (customer_id) references customer(id) on delete cascade
);

create table if not exists order_detail (
    id serial primary key,
    measure_type varchar(255) not null,
    measure_name varchar(255) not null,
    measure_symbol varchar(255) not null,
    quantity_value int not null,
    tax_status varchar(255) not null,
    order_id bigint,
    item_id bigint,
    constraint fk_order_detail_order foreign key (order_id) references orders(id) on delete cascade,
    constraint fk_order_detail_item foreign key (item_id) references item(id) on delete cascade
);

create table if not exists item (
    id serial primary key,
    description varchar(255) not null,
    measure_type varchar(255) not null,
    measure_name varchar(255) not null,
    measure_symbol varchar(255) not null,
    weight_value decimal not null
);

create table if not exists payment (
    id serial primary key,
    amount float not null,
    order_id bigint not null,
    payment_type varchar(255),
    constraint fk_payment_order foreign key (order_id) references orders(id) on delete cascade
);

create table if not exists payment_cash (
    id serial primary key,
    cash_tendered float not null,
    constraint fk_cash_payment foreign key (id) references payment(id) on delete cascade
);

create table if not exists payment_check (
    id serial primary key,
    name varchar(255) not null,
    bank_id varchar(255) not null,
    constraint fk_check_payment foreign key (id) references payment(id) on delete cascade
);

create table if not exists payment_credit (
    id serial primary key,
    number varchar(255) not null,
    type varchar(255) not null,
    exp_date timestamp not null,
    constraint fk_credit_payment foreign key (id) references payment(id) on delete cascade
);

-- rollback drop table if exists customer, address, orders, order_detail, item, payment, payment_cash, payment_check, payment_credit
