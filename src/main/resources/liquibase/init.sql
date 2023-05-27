--liquibase formatted sql

--changeset mr.rifleman:1

create table notification_tasks
(
    id      serial primary key,
    message text   not null,
    chat_id bigint not null,
    notification_ldt timestamp not null
);