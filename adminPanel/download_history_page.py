import pandas as pd
import streamlit as st

import util.dbLayer as dbLayer

# The db size table.
db = dbLayer.DbLayer()
db_tables = db.get_download_files()
db_table_frame = pd.DataFrame(
    db_tables,
    columns=["Account", "File", "Time", "Redirector"]
)
st.dataframe(db_table_frame)
