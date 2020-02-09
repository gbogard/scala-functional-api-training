create table if not exists users (
    id uuid primary key,
    user_name varchar(30) not null,
    display_name varchar(30),
    bio text,
    password varchar(256) not null
);