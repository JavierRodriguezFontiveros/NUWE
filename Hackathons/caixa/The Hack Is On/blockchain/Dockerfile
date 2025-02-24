# Etapa de construcción
FROM maven:3.9.9-eclipse-temurin-21 as build

WORKDIR /app

# Copiar los archivos necesarios para Maven Wrapper
COPY .mvn ./.mvn
COPY mvnw ./

# Dar permisos de ejecución al script mvnw
RUN chmod +x mvnw

# Copiar el pom.xml y el código fuente
COPY pom.xml ./
COPY src ./src

# Usar el Maven Wrapper para construir el proyecto
RUN ./mvnw clean package -DskipTests

# Etapa de ejecución
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copiar el archivo JAR generado en la etapa de construcción
COPY --from=build /app/target/blockchain-0.0.1-SNAPSHOT.jar /app/blockchain-0.0.1-SNAPSHOT.jar

# Exponer el puerto 3000
EXPOSE 3000

# Health Check para verificar el estado de la aplicación
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl --fail http://localhost:3000/health || exit 1

# Ejecutar el archivo JAR
ENTRYPOINT ["java", "-jar", "/app/blockchain-0.0.1-SNAPSHOT.jar"]
