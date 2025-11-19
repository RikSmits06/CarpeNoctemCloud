import streamlit as st
import util.dbLayer as dbLayer

db = dbLayer.DbLayer()


def password_entered():
    email = st.session_state.get("text_input_email", "")
    password = st.session_state.get("text_input_password", "")
    st.session_state["is_logged_in"] = db.valid_login(email, password)


if st.session_state.get("is_logged_in", False):
    pg = st.navigation(
        {
            "Database": [
                st.Page("database_page.py", title="Database")],
            "Tasks": [
                st.Page("index_task_page.py", title="Index Task"),
                st.Page("cleanup_task_page.py", title="Cleanup Task")],
            "Requests":
                [st.Page("daily_request_page.py", title="Daily Requests")]
        })
    st.title(pg.title)
    pg.run()
else:
    with st.form("Credentials"):
        st.text_input("Email", key="text_input_email", autocomplete="email")
        st.text_input("Password", key="text_input_password", type="password",
                      autocomplete="current-password")
        st.form_submit_button("Log in", on_click=password_entered)
