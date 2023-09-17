//buildscript {
//    dependencies {
//        classpath("org.postgresql:postgresql:42.6.0")
//        classpath("ch.qos.logback:logback-classic:1.4.6")
//        classpath("io.github.cdimascio:dotenv-kotlin:6.4.1")
//
////        classpath(files("jar/HikariCP-4.0.3.jar"))
//    }
//}

plugins {
    kotlin("jvm") version "1.8.0"
    application
    java
    id("co.uzzu.dotenv.gradle") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(project(":BL"))
    implementation(project(":DA"))
    implementation(project(":TechUi"))

    implementation("ch.qos.logback:logback-classic:1.4.6")
    implementation("org.slf4j:slf4j-nop:1.7.30")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("com.zaxxer:HikariCP:4.0.3")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(19)
}

application {
    mainClass.set("main.MainKt")
}

val mainClassName = "main.MainKt"

tasks {
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = mainClassName
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
}