-- These constraints are necessary to make sure our data stays correct.

-- You can't start after you have ended.
alter table delete_task_log
    add constraint date_order_check check ( started <= ended );

alter table index_task_log
    add constraint date_order_check check ( started <= ended );

-- Of course, they can't be below zero.
alter table delete_task_log
    add constraint positive_values_check check ( files_deleted >= 0 );

alter table index_task_log
    add constraint positive_values_check check ( files_indexed >= 0 );

-- We should enforce student emails.
alter table account
    add constraint student_email_check check ( email like '%@%.utwente.nl');