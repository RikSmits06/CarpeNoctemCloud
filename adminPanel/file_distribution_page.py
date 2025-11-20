import streamlit as st
import pandas as pd
import plotly.express as px
import util.dbLayer as dbLayer

db = dbLayer.DbLayer()

data = db.file_distribution()
data = {
    "host": [host for [host, _] in data],
    "files": [files for [_, files] in data]
}
df = pd.DataFrame(data)
fig = px.pie(df, values="files", names="host",
             title=f"File Distribution")
st.plotly_chart(fig)
