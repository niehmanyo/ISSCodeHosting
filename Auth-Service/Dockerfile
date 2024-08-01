FROM openjdk:11
FROM redis:latest

LABEL authors="NIEWENYU"

WORKDIR /app

COPY /target/Auth-Service.jar /Auth-Service.jar


ENTRYPOINT ["java", "-jar","Auth-Service.jar"]

EXPOSE 8081
