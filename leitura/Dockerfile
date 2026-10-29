FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 1. Copia a pasta de exceções compartilhada
COPY exceptions/ ./exceptions/

# 2. Copia o pom.xml e o src do microsserviço Leitura
COPY leitura/pom.xml ./leitura/pom.xml
COPY leitura/src ./leitura/src

# 3. Instala a biblioteca Exceptions no Maven
RUN mvn -f /app/exceptions/pom.xml clean install -DskipTests -Dspring-boot.repackage.skip=true

# 4. Compila o microsserviço Leitura
WORKDIR /app/leitura
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/leitura/target/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=1m --retries=2 CMD curl --fail http://localhost:8080 || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]