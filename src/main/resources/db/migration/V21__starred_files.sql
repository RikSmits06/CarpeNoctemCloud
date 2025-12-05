create table starred_remote_files
(
    account_id     int references account (id) on delete cascade,
    remote_file_id bigint references remote_file (id) on delete cascade,
    primary key (account_id, remote_file_id)
);