alter table server
    add constraint unique_host unique (host);

-- This function is created for the trigger which updates the parent directory.
create or replace function directory_update_parent() returns trigger as
$$
begin
    new.parent_directory := (select parent.id
                             from directory parent
                             where regexp_replace(new.path, '[^/]+/$', '') = parent.path
                               and parent.server_id = new.server_id
                             limit 1);
    return new;
end
$$ language plpgsql;

-- Trigger will update the parent directory of all
create trigger directory_update_parent_trigger
    before insert or update
    on directory
    for each row
execute function directory_update_parent();