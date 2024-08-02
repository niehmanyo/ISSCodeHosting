FROM openjdk:11-jre-slim

LABEL authors="NIEWENYU"

WORKDIR /app

COPY /*.jar .

ENTRYPOINT ["java", "-jar","*.jar"]

EXPOSE 8086
