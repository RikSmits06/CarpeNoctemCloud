import pandas as pd
import streamlit as st

import dbLayer

db = dbLayer.DbLayer()

st.title("Carpe Noctem Cloud Admin Panel")

# The database info section.
st.header("Database")

# The db size table.
db_tables = db.get_all_tables()
db_table_frame = pd.DataFrame(
    db_tables,
    columns=["Name", "Size", "Rows"]
)
st.table(db_table_frame)
