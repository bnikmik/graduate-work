-- liquibase formatted sql
-- changeset bnikmik:1

create table comments
(
    id          serial primary key,
    created_at  timestamp,
    text        varchar(255),
    ad_id       integer references ads,
    customer_id integer references customers
);


