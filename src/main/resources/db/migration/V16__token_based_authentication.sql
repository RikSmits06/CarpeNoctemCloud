create table auth_token
(
    token         text primary key,
    account_id    integer   not null,
    expiry        timestamp not null,
    last_accessed timestamp,
    created       timestamp not null default now(),
    foreign key (account_id) references account (id)
);