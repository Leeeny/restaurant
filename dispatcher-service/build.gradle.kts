plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
}

group = "ru.leeeny"
version = "0.0.1-SNAPSHOT"
description = "dispatcher-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

val avroVersion = "1.12.1"
val avroSerializerVersion = "8.2.1"
val springRetryVersion = "2.0.13"

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-kafka")

    implementation("org.springframework.retry:spring-retry:$springRetryVersion")
    implementation("io.confluent:kafka-avro-serializer:$avroSerializerVersion")
    implementation("org.apache.avro:avro:$avroVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-kafka-test")
    testCompileOnly("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}