--liquibase formatted sql

--changeset mr.rifleman:1

create table users
(
    id        serial primary key,
    full_name text not null,
    login     text not null,
    password  text not null,
    city      text,
    role      varchar(255)
);

create table token
(
    id         serial primary key,
    token      text         not null,
    token_type varchar(255) not null,
    expired    boolean,
    revoked    boolean,
    user_id    int references users (id)
);

create table messages
(
    id       serial primary key,
    user_one int       not null,
    user_two int       not null,
    msg_ldt  timestamp not null,
    txt      text      not null,
    is_read  boolean   not null
);

create table posts
(
    id         serial primary key,
    user_owner int       not null references users (id),
    date_time  timestamp not null,
    header     text,
    txt        text      not null,
    pic        text
);

create table friends
(
    id        serial primary key,
    user_from int references users (id),
    user_to     int references users (id),
    status  int not null
);

