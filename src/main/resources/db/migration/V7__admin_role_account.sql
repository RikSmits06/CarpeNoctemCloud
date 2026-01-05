-- We need to be able to distinguish between users and admins.
alter table account
    add column is_admin boolean not null default false;