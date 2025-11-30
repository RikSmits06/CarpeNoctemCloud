# Carpe Noctem Cloud

## General Info

This website is meant as a spiritual successor of the old UTCloud, which sadly does not exist
anymore.
This website is hosted at https://carpenoctem.student.utwente.nl.

## Bugs/Issues

CNCloud is actively being
worked on and not yet feature complete, there might be some bugs.
If you find bugs, feel free to create an issue on GitHub to notify the maintainers.

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

### Email

The email credentials are defined in `application.properties` and this file uses an .env file,
creatively called `.env` to load the credentials.
The `.env` file has to be created manually and contain the entries `EMAIL` and `EMAIL_PASSWORD`.

### Reverse Proxy

It is recommended to run these services behind nginx or another reverse proxy. Use the documentation
of your chosen reverse proxy to configure it properly. It is best to enforce https as logins are
used in the application. Make sure that the database cannot be accessed from the internet. By
default, Postgres disallows connections from locations other than localhost, but it does not hurt to
double-check.

## Features server

### Shell Commands

The main server has a spring shell terminal.
To see the commands type `help` and `help <command>` to learn more.
Most of these commands are used to manually index or create admin accounts.

## FAQ

In this section, some commonly asked questions will be answered, which will explain some decisions
that have been made.

### 1. Why can I not access the site?

The site hosted [here](https://carpenoctem.student.utwente.nl), is meant to only be available from
the campus network.
This means that if you connect to it while using mobile data or while being off campus, one will get
a 403 forbidden access.
This is intended behaviour to prevent people outside the University of
Twente from accessing files.

### 2. Why is the download button giving me a weird zip file?

On the old UTCloud, one could press a button, and it would download the file in the browser.
Because CNCloud does not store the files themselves, but instead searches among files found on file
servers
hosted by other people, it would be inefficient to first download the file to the server and then to
the user.
Until we find a workaround for this, we send the user a zip file containing a url file (or
a similar format depending on the platform). When opening the url file, it will access the file
server directly and one can stream/download the file from there.

### 3. Why does the url file not download anything?

This ties back mostly to question 1, because all file servers are also located on campus.
Most of these file servers are also hidden from outside the campus network.
This means that you cannot directly stream from them if you are somewhere else.

### 4. Why is the server asking for credentials?

The file servers are often required to have credentials in place to prevent random access.
Luckily, they usually leave the password blank, or do not check it all.
This means if it asks for
credentials, it is fine to leave the password empty or fill in a random sequence of characters. 