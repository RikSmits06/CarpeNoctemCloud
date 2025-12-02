import streamlit as st
import util.dbLayer as dbLayer
import pandas as pd

db = dbLayer.DbLayer()

option = st.selectbox("Which endpoint do you want to see?", [x for [x] in db.get_log_endpoints()])

data = [[day, hits] for [day, hits] in db.get_hits_of_endpoint(option)]

days = [day for [day, _] in data]
hits = [hits for [_, hits] in data]
idx = pd.date_range(days[0], days[-1])

data = {}

for i in range(len(days)):
    data[days[i]] = hits[i]

data = pd.Series(data)
data.index = pd.DatetimeIndex(data.index)
data = data.reindex(idx, fill_value=0)

df = pd.DataFrame(data, columns=["Hits"])
print(df)

st.line_chart(df, x_label="Days", y_label="Hits")
