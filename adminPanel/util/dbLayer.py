import os
from os import environ

import psycopg2
from psycopg2._psycopg import connection, cursor
import bcrypt


class DbLayer:
    _hostname = os.getenv("ADMIN_PANEL_DB_HOST", "localhost")
    _conn: connection = psycopg2.connect(
        dbname="cncloud",
        user="cncloud",
        password="12345678",
        host=_hostname
    )

    def __init__(self):
        print(self._hostname)
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
                    order by started desc
                    limit 10;
                    """)
        result = cur.fetchall()
        cur.close()
        return result

    def get_cleanup_tables_limit_10(self):
        cur: cursor = self._conn.cursor()
        cur.execute("""
                    select started, ended, files_deleted
                    from delete_task_log
                    order by started desc
                    limit 10;
                    """)
        result = cur.fetchall()
        cur.close()
        return result

    def valid_login(self, email, password):
        cur: cursor = self._conn.cursor()
        cur.execute("""select password
                       from account
                       where email = %s
                         and email_confirmed
                         and is_admin;""", (email,))
        hashed_password_result = cur.fetchall()
        cur.close()

        if len(hashed_password_result) == 0:
            return False

        return bcrypt.checkpw(password.encode("ascii"),
                              hashed_password_result[0][0].encode("ascii"))

    def get_log_endpoints(self):
        cur: cursor = self._conn.cursor()
        cur.execute("""
                    select distinct endpoint
                    from request_log;
                    """)
        result = cur.fetchall()
        cur.close()
        return result

    def get_hits_of_endpoint(self, endpoint: str):
        cur: cursor = self._conn.cursor()
        cur.execute("""
                    select day, count
                    from request_log
                    where endpoint = %s;
                    """, (endpoint,))
        result = cur.fetchall()
        cur.close()
        return result

    def request_of_day(self, date):
        cur: cursor = self._conn.cursor()
        cur.execute("""
                    select endpoint, count
                    from request_log
                    where day = %s;
                    """, (date,))
        result = cur.fetchall()
        cur.close()
        return result

    def file_distribution(self):
        cur: cursor = self._conn.cursor()
        cur.execute("""
                    select ser.host, count(*)
                    from directory dir,
                         remote_file rf,
                         server ser
                    where rf.directory_id = dir.id
                      and dir.server_id = ser.id
                    group by ser.host;""")
        result = cur.fetchall()
        cur.close()
        return result

    def get_download_files(self):
        cur: cursor = self._conn.cursor()
        cur.execute("""
                    select case when dh.account_id is not null then acc.email else null end,
                           rf.name,
                           dh.time,
                           dh.redirector_used
                    from download_history dh,
                         remote_file rf,
                         account acc
                    where dh.remote_file_id = rf.id
                      and (acc.id = dh.account_id or dh.account_id is null)
                    order by time desc
                    limit 100;
                    """)
        result = cur.fetchall()
        cur.close()
        return result
