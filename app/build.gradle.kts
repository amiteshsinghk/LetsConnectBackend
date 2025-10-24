plugins {
    id("letsConnect.spring-boot-app")
}

group = "com.amitesh"
version = "0.0.1-SNAPSHOT"
description = "LetsConnect Backend"

dependencies {
    implementation(projects.chats)
    implementation(projects.user)
    implementation(projects.notification)
    implementation(projects.common)

    implementation(libs.kotlin.reflect)
    implementation(libs.spring.boot.starter.security)

    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)
    runtimeOnly(libs.postgresql)

}

tasks.withType<Test> {
	useJUnitPlatform()
}
