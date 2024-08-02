FROM openjdk:11-jre-slim

LABEL authors="NIEWENYU"

WORKDIR /app

COPY /Grade-Service.jar .

ENTRYPOINT ["java", "-jar","Grade-Service.jar"]

EXPOSE 8084
