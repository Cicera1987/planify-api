# ==============================
# Stage 1: Build
# ==============================
FROM maven:3.9.11-eclipse-temurin-21-alpine AS build


WORKDIR /app

# Copia o pom.xml e a pasta src
COPY pom.xml .
COPY src ./src

# Build do JAR, pulando os testes (Render faz testes separadamente)
RUN mvn clean package -DskipTests

# ==============================
# Stage 2: Runtime
# ==============================
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copia o JAR gerado no stage anterior
COPY --from=build /app/target/planify-api-0.0.1-SNAPSHOT.jar app.jar

# Porta que a aplicação vai expor
EXPOSE 8080

# Variáveis de ambiente (Render passa DB_URL, DB_USERNAME, DB_PASSWORD)
ENV DB_URL=${DATABASE_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]



