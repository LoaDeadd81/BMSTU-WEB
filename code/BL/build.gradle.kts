plugins {
    kotlin("jvm") version "1.8.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



dependencies {
    testImplementation(kotlin("test"))

    implementation("ch.qos.logback:logback-classic:1.4.6")
    implementation("org.slf4j:slf4j-nop:1.7.30")
    implementation("org.slf4j:slf4j-api:1.7.30")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(19)
}