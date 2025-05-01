FROM openjdk:17-jdk-slim
WORKDIR /app
COPY gateway-service/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080