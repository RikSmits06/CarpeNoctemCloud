import streamlit as st
import util.dbLayer as dbLayer

db = dbLayer.DbLayer()

option = st.selectbox("Which endpoint do you want to see?", [x for [x] in db.get_log_endpoints()])

data = [[day, hits] for [day, hits] in db.get_hits_of_endpoint(option)]

data = {
    "days": [day for [day, _] in data],
    "hits": [hits for [_, hits] in data]
}

st.line_chart(data, x="days", y="hits", x_label="Day", y_label="Hits")
