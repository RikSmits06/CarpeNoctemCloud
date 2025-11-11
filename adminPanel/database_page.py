import pandas as pd
import streamlit as st

import util.dbLayer as dbLayer

# The database info section.
st.title("Database")

# The db size table.
db = dbLayer.DbLayer()
db_tables = db.get_all_tables()
db_table_frame = pd.DataFrame(
    db_tables,
    columns=["Name", "Size", "Rows"]
)
st.table(db_table_frame)
