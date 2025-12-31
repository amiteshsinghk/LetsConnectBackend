plugins {
    id("java-library")
    id("letsConnect.kotlin-common")
}

group = "com.amitesh"
version = "unspecified"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    api(libs.kotlin.reflect)
    api(libs.jackson.module.kotlin)

//    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.amqp)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}