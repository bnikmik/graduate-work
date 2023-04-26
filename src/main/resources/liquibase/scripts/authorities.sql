-- liquibase formatted sql
-- changeset bnikmik:1

create table authorities
(
    username  varchar(50) not null
        references users,
    authority varchar(68) not null
);


