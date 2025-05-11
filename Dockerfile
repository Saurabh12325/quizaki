FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/QuizWebApplication-0.0.1-SNAPSHOT.jar app.jar
#kjf
EXPOSE 8080

CMD ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]