-- liquibase formatted sql
-- changeset bnikmik:1

create table ads
(
    id          serial primary key,
    description varchar(255),
    image       bytea,
    price       integer,
    title       varchar(255),
    customer_id integer references customers
);



