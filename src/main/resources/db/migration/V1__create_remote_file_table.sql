create table remote_file
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         TEXT      NOT NULL,
    download_url TEXT      NOT NULL UNIQUE,
    last_indexed TIMESTAMP NOT NULL
);
