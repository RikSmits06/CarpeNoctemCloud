import streamlit as st

pg = st.navigation(
    {"Database": [
        st.Page("database_page.py", title="Database")]})
pg.run()
