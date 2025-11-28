-- This will delete all accounts, but up to this point, there should not be too many accounts.
-- The only exception is the admin accounts, but we can create new ones.
delete
from account;

-- The reason for the account drop is that we need them to type in a new password.
alter table account
    drop column salt;