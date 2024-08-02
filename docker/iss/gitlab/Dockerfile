FROM openjdk:11-jre-slim

LABEL authors="NIEWENYU"

WORKDIR /app

COPY /Gitlab-Service.jar .

ENTRYPOINT ["java", "-jar","Gitlab-Service.jar"]

EXPOSE 8082
