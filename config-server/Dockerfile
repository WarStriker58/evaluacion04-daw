# Etapa 1: Compilar la aplicación
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen de ejecución ligera
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/config-server-0.0.1-SNAPSHOT.jar app.jar
# Creamos la carpeta interna requerida por la configuración nativa local
RUN mkdir -p /app/config-repo
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
