FROM openjdk:21-slim
COPY target/aijavagrammar.jar /app/aijavagrammar.jar
ENTRYPOINT ["java", "-jar", "/app/aijavagrammar.jar"]
