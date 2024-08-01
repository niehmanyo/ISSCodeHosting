FROM openjdk:11-jre-slim
FROM redis:latest

LABEL authors="NIEWENYU"

WORKDIR /app

COPY /target/Auth-Service.jar /auth.jar


ENTRYPOINT ["java", "-jar","auth.jar"]

EXPOSE 8081
