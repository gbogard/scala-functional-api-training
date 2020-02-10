create table if not exists activities (
      id uuid primary key,
      created_at timestamp not null,
      user_id uuid not null,
      content jsonb not null
);