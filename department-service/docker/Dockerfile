FROM openjdk:17-jdk-slim
WORKDIR /app
COPY department-service/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8084