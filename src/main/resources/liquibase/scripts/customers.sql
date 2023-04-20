-- liquibase formatted sql
-- changeset bnikmik:1

create table customers
(
    id         serial primary key,
    avatar     bytea,
    first_name varchar(255),
    last_name  varchar(255),
    phone      varchar(255),
    username   varchar(255)
);

