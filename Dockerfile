FROM eclipse-temurin:21

WORKDIR /app

RUN apt update && apt upgrade -y
RUN apt install -y maven

EXPOSE 8080

COPY pom.xml .
COPY /src src
COPY .env .
CMD ["mvn", "spring-boot:run", "-Dspring.profiles.include=docker"]

