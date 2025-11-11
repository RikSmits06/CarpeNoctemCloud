create table index_task_log
(
    id            integer generated always as identity primary key,
    started       timestamp not null,
    ended         timestamp not null,
    files_indexed bigint    not null
);

create table delete_task_log
(
    id            integer generated always as identity primary key,
    started       timestamp not null,
    ended         timestamp not null,
    files_deleted bigint    not null
);