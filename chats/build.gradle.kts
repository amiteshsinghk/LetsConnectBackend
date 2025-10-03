plugins {
//    alias(libs.plugins.kotlin.jvm)
//    alias(libs.plugins.kotlin.jpa)
//    alias(libs.plugins.spring.boot)
//    alias(libs.plugins.spring.dependency.management)
//    alias(libs.plugins.kotlin.spring)
    id("java-library")
    id("letsConnect.spring-boot-service")
    kotlin("plugin.jpa")
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
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}