FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY gateway-service/target/*.jar /app.jar
COPY gateway-service/src/main/resources/application-server.yml /app/config/application-server.yml
ENTRYPOINT ["java", "-Dspring.config.location=file:/app/config/application-server.yml", "-jar", "/app.jar"]
EXPOSE 8080
