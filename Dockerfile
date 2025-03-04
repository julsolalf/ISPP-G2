# Etapa de construcción: Se usa JDK 21 y se instala Maven para compilar la aplicación
FROM eclipse-temurin:21 AS builder
WORKDIR /app

# Instalar Maven

RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copiar el archivo pom.xml y descargar las dependencias para aprovechar la caché de Maven.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del código del proyecto y compilar la aplicación sin ejecutar tests.
COPY . .
RUN mvn clean package -DskipTests

# Etapa final: Se ejecuta la aplicación usando JDK 21
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]