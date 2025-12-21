create table user_download_log
(
    uid            INTEGER NOT NULL,
    day            DATE,
    remote_file_id BIGINT  NOT NULL,
    foreign key (uid) references account (id),
    foreign key (remote_file_id) references remote_file (id),
    PRIMARY KEY (uid, day, remote_file_id)
)