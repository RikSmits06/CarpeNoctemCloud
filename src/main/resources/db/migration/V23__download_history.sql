create table download_history
(
    account_id     integer references account (id) on delete cascade,
    remote_file_id bigint    not null references remote_file (id) on delete cascade,
    time           timestamp not null default now()
);