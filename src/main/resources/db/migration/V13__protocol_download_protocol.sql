alter table server
    add column download_prefix text;

update server
set download_prefix='file'
where protocol = 'smb';

alter table server
    alter column download_prefix set not null;