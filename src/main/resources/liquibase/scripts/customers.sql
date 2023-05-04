-- liquibase formatted sql
-- changeset bnikmik:1

create table customers
(
    id         serial       not null primary key,
    username   varchar(100) unique not null,
    password   varchar(255) not null,
    first_name varchar(100) not null,
    last_name  varchar(100) not null,
    avatar     bytea,
    phone      varchar(100) not null,
    role       varchar(255),
    enabled    boolean      not null
);

