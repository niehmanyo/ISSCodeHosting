FROM openjdk:11-jre-slim

LABEL authors="NIEWENYU"

WORKDIR /app

COPY /CodeHosting-Gateway.jar .

ENTRYPOINT ["java", "-jar","CodeHosting-Gateway.jar"]

EXPOSE 8088
