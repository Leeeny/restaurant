plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.ben.manes)
}

group = "ru.leeeny"
version = "0.0.1-SNAPSHOT"
description = "menu-service"

java {
    sourceCompatibility = JavaVersion.VERSION_25
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.boot.data.jpa)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.boot.web)

    implementation(libs.postgresql)
    implementation(libs.spring.boot.starter.flyway)
    implementation(libs.flyway.postgresql)
    // implementation(libs.hypersistence) //TODO: что-то с ним решить
    implementation(libs.mapstruct)
    implementation(libs.springdoc)

    compileOnly(libs.lombok)
    compileOnly(libs.mapstruct)

    annotationProcessor(libs.hibernate.jpamodelgen)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.spring.boot.configuration.processor)
    annotationProcessor(libs.mapstruct.processor)
    annotationProcessor(libs.lombok.mapstruct.binding)

    testImplementation(libs.spring.boot.test)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.spring.boot.webflux)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
