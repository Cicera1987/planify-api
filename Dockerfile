FROM eclipse-temurin:21-jdk
WORKDIR /app
EXPOSE 8080

# Nome exato do JAR gerado
ARG JAR_FILE=target/planify-api-0.0.1-SNAPSHOT.jar

# Copia o JAR para dentro do container
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]



