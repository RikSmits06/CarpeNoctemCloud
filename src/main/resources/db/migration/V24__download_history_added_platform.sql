delete
from download_history;

alter table download_history
    add column redirector_used text not null;