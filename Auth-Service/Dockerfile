FROM openjdk:11
FROM redis:latest

LABEL authors="NIEWENYU"

WORKDIR /app

COPY /target/Auth-Service.jar /auth.jar


ENTRYPOINT ["java", "-jar","auth.jar"]

EXPOSE 8081
