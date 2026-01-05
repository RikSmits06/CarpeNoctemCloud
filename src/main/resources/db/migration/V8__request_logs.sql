-- We want to add request analysis per endpoint.

create table request_log
(
    day      DATE,
    endpoint TEXT,
    count    BIGINT NOT NULL check ( count >= 0 ),
    PRIMARY KEY (day, endpoint)
);