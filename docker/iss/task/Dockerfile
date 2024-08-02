FROM openjdk:11-jre-slim

LABEL authors="NIEWENYU"

WORKDIR /app

COPY /Auth-Service.jar .

ENTRYPOINT ["java", "-jar","Auth-Service.jar"]

EXPOSE 8083
