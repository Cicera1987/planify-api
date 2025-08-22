FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clear install


FROM openjdk:21-jdk-slim

EXPOSE 8080

ARG JAR_FILE=target/planify-api-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "app.jar"]



