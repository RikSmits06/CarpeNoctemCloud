-- Function which is triggered everytime we add or update a remote_file row.
create or replace function remote_file_search_vector_trigger() returns trigger as
$$
begin
    new.search_vector := setweight(to_tsvector(regexp_replace(new.name,
                                                              '[.\-,&^\[\]{}#*+=;:<>/?|\\"!@%$()]',
                                                              ' ', 'g')), 'A') ||
                         setweight(
                                 to_tsvector(coalesce((select name
                                                       from category
                                                       where id = new.category_id
                                                       limit 1), '')),
                                 'B') || setweight(to_tsvector(coalesce(
            (select replace(path, '/', ' ') from directory where id = new.directory_id limit 1),
            '')), 'B');
    return new;
end
$$ language plpgsql;

-- Configures a trigger on update or insert so that the search_vector stays up to date.
create or replace trigger search_vector_creation
    before insert or update
    on remote_file
    for each row
execute function remote_file_search_vector_trigger();