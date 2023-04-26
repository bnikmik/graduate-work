-- liquibase formatted sql
-- changeset bnikmik:1

create table users
(
    username varchar(50) not null
        primary key,
    password varchar(68) not null,
    enabled  boolean     not null
);


