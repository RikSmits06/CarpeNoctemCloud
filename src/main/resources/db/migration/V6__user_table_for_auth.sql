create table account
(
    id              INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name            TEXT        NOT NULL,
    email           TEXT UNIQUE NOT NULL,
    email_confirmed BOOLEAN     NOT NULL default false,
    password        TEXT        NOT NULL,
    salt            TEXT        NOT NULL
);