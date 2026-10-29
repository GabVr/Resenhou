FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 1. Copia a biblioteca de exceções compartilhada
COPY exceptions/ ./exceptions/

# 2. Copia a pasta inteira do microsserviço Usuarios
COPY usuarios/pom.xml ./usuarios/pom.xml
COPY usuarios/ ./usuarios/

# 3. Instala a biblioteca Exceptions no Maven
RUN mvn -f /app/exceptions/pom.xml clean install -DskipTests -Dspring-boot.repackage.skip=true

# 4. Entra na pasta do microsserviço e compila
WORKDIR /app/usuarios

RUN mvn clean package -DskipTests


# Etapa de execução (Runner)
FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/usuarios/target/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=1m --retries=2 CMD curl --fail http://localhost:8080 || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]