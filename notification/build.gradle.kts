plugins {
    id("java-library")
    id("letsConnect.spring-boot-service")
    kotlin("plugin.jpa")
//    alias(libs.plugins.kotlin.jvm)
//    alias(libs.plugins.kotlin.jpa)
//    alias(libs.plugins.spring.boot)
//    alias(libs.plugins.spring.dependency.management)
//    alias(libs.plugins.kotlin.spring)
}

group = "com.amitesh"
version = "unspecified"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation(projects.common)
    implementation(libs.spring.boot.starter.amqp)
    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.spring.boot.starter.mail)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}