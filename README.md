# Carpe Noctem Cloud

## Setup

This part of the readme is dedicated to hosting this website yourself.

### Database

The DMS used is Postgres. The server would thus need a Postgres instance running.
The default account name is `cncloud`, with password equal to `12345678`. This might seem insecure,
but the database should not be directly accessible from the internet but only through the
application. The application connects to the database with the name `cncloud`. If there exists a
user and empty database with the given values, the application should automatically run database
migrations when it starts up. If the migrations are done, the database is ready for use.

### Admin Panel

The admin panel is a Python server made with the Streamlit framework. To install the dependencies,
run `pip install -r requirements.txt` in the adminPanel directory. Then to run it use the command
`streamlit run server.py`. It will now be running on http://localhost:5001.

### Main Server

The main server is written using Java and the Spring Boot framework. To run it, use `mvn package` to
compile it into a jar file. Use `java -jar <jarFile>` to run the server. It should automatically
perform the database migrations mentioned in the database section.

### Reverse Proxy

It is recommended to run these services behind nginx or another reverse proxy. Use the documentation
of your chosen reverse proxy to configure it properly. It is best to enforce https as logins are
used in the application. Make sure that the database cannot be accessed from the internet. By
default, Postgres disallows connections from locations other than localhost but it does not hurt to
double-check.

## Features

### Shell Commands

The main server has a spring shell terminal. To see the commands type `help` and `help <command` to
learn more.