# 🛠 Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Mejor caché: primero pom y dependencias
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Código
COPY src ./src

# Build
RUN mvn -B -q clean package -DskipTests

# 🚀 Etapa de runtime (Ubuntu 22.04 'jammy')
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
EXPOSE 8080

# Copiamos el JAR
COPY --from=build /app/target/colegiocuotas-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]