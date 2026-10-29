FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 1. Copia a pasta de exceções compartilhada
COPY exceptions/ ./exceptions/

# 2. Copia a pasta do microsserviço Catalogo
COPY catalogo/pom.xml ./catalogo/pom.xml
COPY catalogo/ ./catalogo/

# 3. Instala a biblioteca Exceptions no Maven
RUN mvn -f /app/exceptions/pom.xml clean install -DskipTests -Dspring-boot.repackage.skip=true

# 4. Compila o microsserviço Catalogo
WORKDIR /app/catalogo

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/catalogo/target/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=1m --retries=2 CMD curl --fail http://localhost:8080 || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]