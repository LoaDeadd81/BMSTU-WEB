val logback_version: String by project
val slf4j_version: String by project


buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.6.0")
    }
}

plugins {
    kotlin("jvm") version "1.9.0"
    id("org.flywaydb.flyway") version "10.0.0"
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
    implementation("org.jetbrains.exposed:exposed-core:0.44.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.44.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.44.0")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.slf4j:slf4j-nop:$slf4j_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")
    testImplementation("com.radcortez.flyway:flyway-junit5-extension:1.4.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(20)
}

flyway {
    url = "jdbc:postgresql://localhost:65432/back"
    user = "main_user"
    password = "main_user"
    locations = arrayOf("filesystem:src/main/resources/db/migration/")
    cleanDisabled = false
}



