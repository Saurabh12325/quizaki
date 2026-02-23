## Step 1: Build JAR using Maven
#FROM maven:3.9.4-eclipse-temurin-17 AS builder
#WORKDIR /app
#COPY . .
#RUN mvn clean package -DskipTests

# Step 2: Run JAR using slim Java
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/QuizWebApplication-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]
#daf