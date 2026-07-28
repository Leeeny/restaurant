import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    java
    idea
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.ben.manes)
    alias(libs.plugins.openapi)
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
    implementation(libs.jackson.databind.nullable)
    implementation(libs.spring.boot.data.jpa.test)

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
    testImplementation(libs.spring.boot.webtestclient)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootBuildImage>("bootBuildImage") {
    imageName.set(project.name)

    environment.put("BP_JVM_VERSION", "25")
    environment.put("BP_HEALTH_CHECKER_ENABLED", "true")
}

/*
______________________________________
============ API GENERATION ==========
______________________________________
 */

val openApiDir = file("${rootDir}/openapi")
val foundSpecifications = openApiDir.listFiles { file -> file.extension in listOf("yaml", "yml") }
    ?: emptyArray()

logger.lifecycle(
    "Found ${foundSpecifications.size} specifications: " +
            foundSpecifications.joinToString { it.name })

foundSpecifications.forEach { specFile ->
    val specName = specFile.nameWithoutExtension
    val outDir = generatedSourcesDir(specName)
    val taskName = buildGenerateApiTaskName(specName)
    val basePackage = "ru.leeeny.menuservice"   // ← жёстко тот же пакет, что у Entity

    logger.lifecycle("Register task $taskName for $specName -> $basePackage")

    tasks.register(taskName, GenerateTask::class.java) {
        description = "Generates the Open API sources for $specName"
        group = "openapi"
        generatorName.set("spring")
        inputSpec.set(specFile.invariantSeparatorsPath)
        outputDir.set(outDir)

        configOptions.set(
            mapOf(
                "interfaceOnly" to "true",
                "skipDefaultInterface" to "true",
                "useSpringBoot3" to "true",
                "useJakartaEe" to "true",
                "useBeanValidation" to "true",
                "openApiNullable" to "false", //TODO: версии spring boot и jacksonNullable не совместимы
                "useOptional" to "true", //TODO: потом убрать
                "dateLibrary" to "java8",
                "useTags" to "true",
                "hideGenerationTimestamp" to "true",
                "apiPackage" to "$basePackage.api",
                "modelPackage" to "$basePackage.dto",
                "useOneOfInterfaces" to "true"
            )
        )

//        schemaMappings.set(
//            mapOf(
//                "SortBy" to "$basePackage.dto.SortBy"
//            )
//        )

        doFirst {
            logger.lifecycle("$taskName: starting generation from ${specFile.name}")
        }
    }
}

fun generatedSourcesDir(specName: String): Provider<String> =
    layout.buildDirectory
        .dir("generated/openapi/$specName")
        .map { it.asFile.invariantSeparatorsPath }

fun defineJavaPackageName(name: String): String {
    val beforeDash = name.substringBefore("-")
    val match = Regex("^[a-z]+").find(beforeDash)
    return (match?.value ?: beforeDash).lowercase()
}

fun toPascalCase(name: String): String =
    name.split(Regex("[-_]+"))
        .filter { it.isNotEmpty() }
        .joinToString("") { it.replaceFirstChar(Char::uppercase) }

fun buildTaskName(prefix: String, name: String): String = "$prefix-${toPascalCase(name)}"
fun buildGenerateApiTaskName(name: String) = buildTaskName("generate", name)
fun buildJarTaskName(name: String) = buildTaskName("jar", name)

val specNames = foundSpecifications.map { it.nameWithoutExtension }

sourceSets.named("main") {
    specNames.forEach { specName ->
        java.srcDir(
            layout.buildDirectory.dir("generated/openapi/$specName/src/main/java")
        )
    }
}

tasks.register("generateAllOpenApi") {
    group = "openapi"
    description = "Generates the OpenAPI sources for all specifications"
    specNames.forEach { specName ->
        dependsOn(buildGenerateApiTaskName(specName))
    }
    doLast {
        logger.lifecycle("generateAllOpenApi: all specifications have been generated")
    }
}

tasks.named("compileJava") {
    dependsOn("generateAllOpenApi")
}