FROM openjdk:17-jdk-slim
WORKDIR /app
COPY position-service/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8083