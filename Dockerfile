## Build stage
#FROM gradle:8.4-jdk17 AS builder
#WORKDIR /app
#
## Copy Gradle files and pre-fetch dependencies
#COPY build.gradle settings.gradle ./
#RUN gradle clean build --no-daemon || true
#
## Copy source code and build the project
#COPY src ./src
#RUN gradle  build -x test --no-daemon
#
## Set permissions
#USER root
#RUN chown -R root:root ./
#USER makara
#
## Run stage
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY --from=builder /app/build/libs/*.jar /app/myapp.jar
#EXPOSE 8888
#ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]
#
#


# Builder stage
FROM gradle:8.4-jdk17-alpine AS builder
WORKDIR /app
COPY . .
#Note: Skipping tests is generally not recommended except for diagnostic purposes.
RUN gradle build --no-daemon -x test

# Build the application
#RUN gradle build --no-daemon

# Final stage
FROM openjdk:17-alpine
WORKDIR /app
# Copy the built JAR from the builder stage and rename it for simplicity
COPY --from=builder /app/build/libs/*.jar myapp.jar
EXPOSE 8888
VOLUME /home/ite/media
VOLUME /keys
# Now you can reference a fixed name in the ENTRYPOINT
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "myapp.jar"]
