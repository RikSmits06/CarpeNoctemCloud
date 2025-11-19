import streamlit as st
import pandas as pd
import plotly.express as px
import util.dbLayer as dbLayer

db = dbLayer.DbLayer()

today = st.date_input("")
data = [[count, endpoint] for endpoint, count in db.request_of_day(today)]

if len(data) != 0:
    data = {
        "Endpoint": [e for _, e in data],
        "Hits": [c for c, _ in data]
    }

    df = pd.DataFrame(data)

    fig = px.pie(df, values="Hits", names="Endpoint")
    st.plotly_chart(fig)
else:
    st.write("No data on this day.")