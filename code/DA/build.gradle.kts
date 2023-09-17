buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.6.0")
    }
}

plugins {
    kotlin("jvm") version "1.8.0"
    application
    id("org.flywaydb.flyway") version "9.16.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":BL"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    implementation("org.jetbrains.exposed:exposed-core:0.40.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.40.1")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.6")
    implementation("org.slf4j:slf4j-nop:1.7.30")
    implementation("org.slf4j:slf4j-api:1.7.30")
    testImplementation("com.radcortez.flyway:flyway-junit5-extension:1.4.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(19)
}

application {
    mainClass.set("MainKt")
}

flyway {
    url =
        "jdbc:" + env.fetch("DB_CONNECT") + "://" + env.fetch("DB_HOST") + ":" + env.fetch("DB_PORT") + "/" + env.fetch(
            "DB_NAME"
        )
    user = env.fetch("DB_USER")
    password = env.fetch("DB_PASSWORD")
    locations = arrayOf("filesystem:src/main/resources/db/migration/")
    cleanDisabled = false
}



