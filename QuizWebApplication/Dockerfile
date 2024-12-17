FROM openjdk:17-jdk AS build
WORKDIR /Quizmain
COPY pom.xml .
COPY src src

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Set execution permission for the Maven wrapper
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final Docker image using OpenJDK 19
FROM openjdk:17-jdk
VOLUME /tmp

# Copy the JAR from the build stage
COPY --from=build /Quizmain/QuizWebApplication/target/* app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080

