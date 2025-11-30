-- We need these codes to confirm email addresses in the accounts table.
create table email_confirmation_token
(
    account_id int primary key,
    token      text      not null unique,
    expiry     timestamp not null,
    foreign key (account_id) references account (id)
);