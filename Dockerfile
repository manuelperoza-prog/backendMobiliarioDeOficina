# Etapa 1: Construcción (Build) con Maven
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Run)
FROM eclipse-temurin:25-jdk
WORKDIR /app

# Copia el archivo .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto configurado en application.properties
EXPOSE 8080

# Optimización de memoria crítica para la capa gratuita (512MB)
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xss512k -XX:MaxRAMPercentage=75.0"

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]