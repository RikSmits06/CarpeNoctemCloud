FROM python:3.13-slim
WORKDIR /app

RUN apt update && apt upgrade -y
RUN apt install -y gcc libpq-dev


COPY requirements.txt .
RUN pip3 install -r requirements.txt

COPY . .

EXPOSE 5001

ENTRYPOINT ["streamlit", "run", "server.py"]