-- We start by deleting the old cached data because it is more work to migrate this than to just reindex.
-- This is done in relation to issue #19 (https://github.com/RikSmits06/CarpeNoctemCloud/issues/19).
drop table remote_file;

create table server
(
    id       int primary key generated always as identity,
    host     text not null,
    protocol text not null
);

create table directory
(
    id               bigint primary key generated always as identity,
    path             text not null,
    parent_directory bigint default null,
    server_id        int  not null,
    foreign key (parent_directory) references directory (id),
    foreign key (server_id) references server (id),
    constraint unique_server_path unique (server_id, path)
);

create table remote_file
(
    id            bigint primary key generated always as identity,
    directory_id  bigint    not null,
    search_vector tsvector  not null default to_tsvector(''),
    name          text      not null,
    category_id   int                default null,
    last_indexed  timestamp not null default now(),
    foreign key (category_id) references category (id),
    foreign key (directory_id) references directory (id),
    constraint unique_file unique (directory_id, name)
);

-- Function returning trigger which will update the category of an item if it is null.
create or replace function remote_file_category_trigger() returns trigger as
$$
begin
    new.category_id = coalesce(new.category_id, (select c.id
                                                 from category c
                                                 where split_part(new.name, '.', -1) = any
                                                       (c.matching_extensions)
                                                 limit 1));
    return new;
end
$$ language plpgsql;

-- Enables a trigger when inserting a new remote_file so that the category automatically gets found.
create trigger category_labeling
    before insert or update
    on remote_file
    for each row
execute function remote_file_category_trigger();

-- Function which is triggered everytime we add or update a remote_file row.
create or replace function remote_file_search_vector_trigger() returns trigger as
$$
begin
    new.search_vector := setweight(to_tsvector(new.name), 'A') ||
                         setweight(
                                 to_tsvector(coalesce((select name
                                                       from category
                                                       where id = new.category_id
                                                       limit 1), '')),
                                 'B') || setweight(to_tsvector(coalesce(
            (select path from directory where id = new.directory_id limit 1), '')), 'B');
    return new;
end
$$ language plpgsql;

-- Configures a trigger on update or insert so that the search_vector stays up to date.
create trigger search_vector_creation
    before insert or update
    on remote_file
    for each row
execute function remote_file_search_vector_trigger();