create table if not exists posts (
    id uuid primary key,
    created_at timestamp,
    author_id uuid,
    content text
);