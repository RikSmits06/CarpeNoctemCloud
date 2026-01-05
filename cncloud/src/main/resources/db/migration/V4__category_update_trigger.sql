/*
 We only had the trigger on insert, but we also need it on update.
 This is because if we add a category, the cached files don't get updated.
 */
CREATE OR REPLACE TRIGGER category_labeling
    BEFORE INSERT OR UPDATE
    ON remote_file
    FOR EACH ROW
EXECUTE FUNCTION remote_file_category_trigger();