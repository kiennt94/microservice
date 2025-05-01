FROM openjdk:17-jdk-slim
WORKDIR /app
COPY config-server/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8888