# 🛠 Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos solo pom.xml y descargamos dependencias (mejor caché)
COPY pom.xml .
RUN mvn dependency:go-offline

# Ahora copiamos el resto del código
COPY src ./src

# Construimos el proyecto
RUN mvn clean package -DskipTests

# 🚀 Etapa de ejecución con slim-buster
FROM eclipse-temurin:17-jre-slim-buster

WORKDIR /app

EXPOSE 8080

# Copiamos el JAR desde la etapa de build
COPY --from=build /app/target/spring-boot-backend-optica-0.0.1-SNAPSHOT.jar app.jar

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
