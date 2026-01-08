FROM eclipse-temurin:21

WORKDIR /app

RUN apt update && apt upgrade -y
RUN apt install -y maven

EXPOSE 8080

COPY pom.xml .
RUN mvn verify clean --fail-never
COPY /src src
COPY .env .
RUN mvn -Dmaven.test.skip package
CMD ["java", "-jar", "-Dspring.profiles.include=docker", "target/CarpeNoctemCloud-0.0.1-SNAPSHOT.jar"]