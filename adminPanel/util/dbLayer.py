import psycopg2
from psycopg2._psycopg import connection, cursor

_conn: connection | None = None


class DbLayer:
    _conn: connection = psycopg2.connect(
        dbname="cncloud",
        user="cncloud",
        password="12345678",
        host="localhost"
    )

    def __init__(self):
        pass

    def get_all_tables(self):
        cur: cursor = self._conn.cursor()
        cur.execute("""
                    select table_name,
                           pg_size_pretty(pg_table_size('"public"."' || table_name || '"')) as size
                    from information_schema.tables
                    where table_schema = 'public';
                    """)
        ret = cur.fetchall()
        cur.close()

        for i in range(len(ret)):
            cur = self._conn.cursor()
            cur.execute(f"select count(*) from \"{(ret[i][0])}\"")
            count = cur.fetchone()[0]
            tmp = list(ret[i])
            tmp.append(count)
            ret[i] = tuple(tmp)
            cur.close()
        return ret

    def get_indexed_tables_limit_10(self):
        cur: cursor = self._conn.cursor()
        cur.execute("""
                    select started, ended, files_indexed
                    from index_task_log
                    order by started desc limit 10;
                    """)
        result = cur.fetchall()
        cur.close()
        return result

    def get_cleanup_tables_limit_10(self):
        cur: cursor = self._conn.cursor()
        cur.execute("""
                    select started, ended, files_deleted
                    from delete_task_log
                    order by started desc limit 10;
                    """)
        result = cur.fetchall()
        cur.close()
        return result

    def valid_login(self, email, password):
        cur: cursor = self._conn.cursor()
        cur.execute("""select exists(select
                                     from account
                                     where email = %s
                                       and cast(sha256(cast(%s || '|' || salt as bytea)) as text) =
                                           password
                                       and email_confirmed
                                       and is_admin);""", (email, password))
        result = cur.fetchone()[0]
        cur.close()
        return result
