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
