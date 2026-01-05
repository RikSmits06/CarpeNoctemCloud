/*
 Creates a new category table and adds some default categories which should be built on later.
 "remote_file" will reference this table, and there exists a trigger which will give it a category on insert.

 */


-- Table which holds the categories that exist.
create table category
(
    id                  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                TEXT   NOT NULL UNIQUE,
    matching_extensions TEXT[] NOT NULL
);

-- Some categories that we already have.
insert into category(name, matching_extensions)
values ('Film', '{"mkv", "mp4", "ogv", "ogg", "avi", "mov", "wmv", "mpeg", "mpg", "m4v"}'),
       ('Audio', '{"mp3", "wav", "webm", "au"}'),
       ('Pictures', '{"jpeg", "gif", "png", "jpg", "svg", "webp", "bmp"}');

-- We need to keep track of which categories we have on each file.
alter table remote_file
    add column category_id INTEGER references category (id);

-- Updates current remote_file to add categories.
update remote_file rf
set category_id =
        (select c.id
         from category c
         where split_part(rf.name, '.', -1) = any
               (c.matching_extensions)
         limit 1)
where rf.category_id is null;

-- Function returning trigger which will update the category of an item if it is null.
CREATE FUNCTION remote_file_category_trigger() RETURNS TRIGGER AS
$$
BEGIN
    new.category_id = coalesce(new.category_id, (select c.id
                                                 from category c
                                                 where split_part(new.name, '.', -1) = any
                                                       (c.matching_extensions)
                                                 limit 1));
    return new;
END
$$ LANGUAGE plpgsql;

-- Enables a trigger when inserting a new remote_file so that the category automatically gets found.
CREATE TRIGGER category_labeling
    BEFORE INSERT
    ON remote_file
    FOR EACH ROW
EXECUTE FUNCTION remote_file_category_trigger();