# Сейчас вместо него используется Cloud Native Buildpacks

FROM gradle:9.6.0-jdk25 AS build

WORKDIR /home/gradle/project
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle/libs.versions.toml ./gradle/libs.versions.toml
COPY src ./src
COPY openapi ./openapi

RUN gradle --no-daemon clean build -x test

FROM eclipse-temurin:25-jre

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8091
CMD ["java", "-jar", "app.jar"]