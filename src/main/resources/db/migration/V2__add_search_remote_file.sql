-- Adds the search_vector column to the remote file table.
ALTER TABLE remote_file
    ADD COLUMN search_vector tsvector not null default to_tsvector('');

-- Updates existing rows to have a good search_vector.
UPDATE remote_file
SET search_vector = setweight(to_tsvector(download_url), 'B') ||
                    setweight(to_tsvector(name), 'A')
WHERE remote_file.search_vector = to_tsvector('');

-- Function which is triggered everytime we add or update a remote_file row.
CREATE FUNCTION remote_file_search_vector_trigger() RETURNS TRIGGER AS
$$
BEGIN
    NEW.search_vector := setweight(to_tsvector(NEW.download_url), 'B') ||
                         setweight(to_tsvector(NEW.name), 'A');
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

-- Configures a trigger on update or insert so that the search_vector stays up to date.
CREATE TRIGGER search_vector_creation
    BEFORE INSERT OR UPDATE
    ON remote_file
    FOR EACH ROW
EXECUTE FUNCTION remote_file_search_vector_trigger();