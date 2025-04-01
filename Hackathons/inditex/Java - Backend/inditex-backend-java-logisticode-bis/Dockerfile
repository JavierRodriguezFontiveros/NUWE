# Usa una imagen base de OpenJDK 21
FROM openjdk:21-jdk-slim as build

# Instalar wget y unzip para descargar y descomprimir Maven
RUN apt-get update && apt-get install -y wget unzip \
    && wget https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip \
    && unzip apache-maven-3.9.9-bin.zip \
    && mv apache-maven-3.9.9 /opt/maven \
    && ln -s /opt/maven/bin/mvn /usr/bin/mvn

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo pom.xml y las dependencias
COPY pom.xml .

# Descargar las dependencias de Maven
RUN mvn dependency:go-offline

# Copiar todo el código fuente
COPY src ./src

# Ejecutar la compilación y empaquetar la aplicación
RUN mvn clean package -DskipTests

# Etapa final: Imagen para ejecutar la aplicación
FROM openjdk:21-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto 3000
EXPOSE 3000

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
