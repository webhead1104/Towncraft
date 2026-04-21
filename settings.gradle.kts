rootProject.name = "Towncraft"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.foxikle.dev/cytonic")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

//include("tools:wiki-scraper")
//include(":platforms:common")
//include(":platforms:cytosis")