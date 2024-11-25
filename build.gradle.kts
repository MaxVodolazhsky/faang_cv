import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("java")
    id("org.openapi.generator") version "6.2.1"
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.6"
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
    }
}

group = "org.faang"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.telegram:telegrambots-spring-boot-starter:6.7.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("io.github.openfeign:feign-okhttp")
    implementation("io.github.openfeign:feign-jackson")


    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    compileOnly("javax.servlet:servlet-api:2.5")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("org.postgresql:postgresql:42.7.3")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

}

val generateSources = tasks.create("generateSources") {
    group = "BUILD"
    description = "Генерация ресурсов по OpenApi-спецификациям"
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
tasks.compileJava {
    dependsOn(generateSources)
}

val rootPackage = "org.faang.cv.bot"
val rootOpenApi = "$rootDir/openapi"
mapOf(
        "$rootOpenApi/openapi-provider-access-token.yml" to "TokenProvider",
        "$rootOpenApi/openapi-gigachat.yml" to "GigaChatClient"
).forEach { (specification, name) ->
    val taskName = "openApiGenerate$name"
    val targetPackage = name.toLowerCase()

    task(taskName, type = GenerateTask::class) {
        group = "openapi tools"
        generatorName.set("spring")
        inputSpec.set(specification)
        outputDir.set("$buildDir/generated-sources/openapi/client")
        apiPackage.set("$rootPackage.generated.$targetPackage.api")
        modelPackage.set("$rootPackage.generated.$targetPackage.model")
        configOptions.putAll(
                mapOf(
                        "useTags" to "true",
                        "dateLibrary" to "java11",
                        "serializationLibrary" to "jackson",
                        "useBeanValidation" to "true",
                        "interfaceOnly" to "true",
                        "skipDefaultInterface" to "true",
                        "performBeanValidation" to "true",
                        "openApiNullable" to "false",
                        "documentationProvider" to "none"
                )
        )
        generateSources.dependsOn(taskName)
        sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).java.srcDir("$buildDir/generated-sources/openapi/client/src/main/java")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
