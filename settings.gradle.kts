pluginManagement {
    includeBuild("build-logic")
	repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
		maven { url = uri("https://repo.spring.io/snapshot") }
		gradlePluginPortal()
	}
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "letsConnect"
include("app")
include("user")
include("chats")
include("notification")
include("common")