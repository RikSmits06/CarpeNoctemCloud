alter table account
    add constraint minimum_length check ( length(trim(name)) > 0);