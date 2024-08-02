FROM openjdk:11-jre-slim

LABEL authors="NIEWENYU"

WORKDIR /app

COPY /Task-Service.jar .

ENTRYPOINT ["java", "-jar","Task-Service.jar"]

EXPOSE 8083
