import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("org.openapi.generator") version "6.2.1"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "org.entur"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.google.cloud:google-cloud-iamcredentials:2.3.6")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.11.0")
    implementation("com.google.api-client:google-api-client:2.0.0")
    implementation("com.google.cloud:google-cloud-bigquery:2.17.0")
    implementation("com.auth0:java-jwt:4.1.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
}

val serverPackage = "org.entur.server"
val apiSpecLocation = "src/main/resources/specs/resolve-1.0.0.yaml"
val serverOutputDir = project.layout.buildDirectory.dir("generated-api")

tasks.register("validateSpec", org.openapitools.generator.gradle.plugin.tasks.ValidateTask::class) {
    inputSpec.set(project.file(apiSpecLocation).path)
    recommend.set(true)
}

tasks.register("generateServer", org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
    generatorName.set("kotlin-spring")
    inputSpec.set(project.file(apiSpecLocation).path)
    outputDir.set(serverOutputDir.get().toString())
    apiPackage.set("$serverPackage.api")
    invokerPackage.set(serverPackage)
    modelPackage.set("$serverPackage.viewmodel")
    configOptions.set(
        mapOf(
            "documentationProvider" to "none",
            "dateLibrary" to "java8",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "enumPropertyNaming" to "UPPERCASE"
        )
    )
}

sourceSets {
    val main by getting
    main.java.srcDir("${serverOutputDir.get()}/src/main/kotlin")
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    filter {
        exclude { entry ->
            entry.file.toString().contains("generated-api")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }

    dependsOn("validateSpec")
    dependsOn("generateServer")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("application.jar")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    exclude("org.entur.tokenexchange.e2e**")
}
