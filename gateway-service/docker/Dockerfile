FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY gateway-service/target/*.jar /app.jar
COPY gateway-service/src/main/resources/application.yml /config/application.yml
COPY gateway-service/src/main/resources/application-server.yml /app/config/application-server.yml
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080