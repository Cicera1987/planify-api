# Etapa de build
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia o pom.xml e baixa dependências (para cache eficiente)
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/planify-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]




