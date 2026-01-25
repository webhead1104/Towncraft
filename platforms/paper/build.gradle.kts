import me.modmuss50.mpp.ReleaseType

plugins {
    id("java")
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.gradleup.shadow") version "9.3.1"
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
    id("net.kyori.blossom") version "2.2.0"
}
version = project.findProperty("plugin_version") as String
val minecraftVersion = findProperty("minecraft_version") as String

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":platforms:common"))
    implementation(project(":platforms:common")) {
        isTransitive = false
        exclude("com.google")
        exclude("org.apache")
        exclude("org.intellij")
        exclude("org.jetbrains")
        exclude("org.slf4j")
        exclude("org.jspecify")
        exclude("net.kyori")
    }

    compileOnly(libs.lamp.bukkit)
    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")
    implementation(libs.bstats)
    annotationProcessor(libs.autoService.processor)

    //shadow these in since paper class loading is weird
    implementation(libs.configurate.gson)
    implementation(libs.configurate.yaml)
    implementation(libs.inventoryFramework.api)
    implementation(libs.inventoryFramework.platform)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

sourceSets {
    main {
        blossom {
            javaSources {
                val deps = provider {
                    val commonDeps = project(":platforms:common")
                        .configurations.getByName("api")
                        .dependencies
                        .filterIsInstance<ModuleDependency>()
                        .filter { it.group != "org.spongepowered" }

                    val platformDeps = configurations.getByName("compileOnly")
                        .dependencies
                        .filterIsInstance<ModuleDependency>()
                        .filter { it.group != "io.papermc.paper" && it.group != "me.webhead1104" }

                    (commonDeps + platformDeps).map { "${it.group}:${it.name}:${it.version}" }
                }
                val depsString: String = deps.get().joinToString(separator = "\n        ") {
                    """resolver.addDependency(new Dependency(new DefaultArtifact("${
                        it.replace(
                            ".",
                            "$"
                        )
                    }".replace("$", ".")), null));"""
                }.trimMargin()
                properties.put("depString", depsString)
            }
        }
    }
}

tasks {
    publishMods {
        file = shadowJar.get().archiveFile
        changelog.set(
            providers.environmentVariable("CHANGELOG")
                .orElse("## What's Changed\n\nSee commit history for details.")
        )
        val versionString = version.get()
        type.set(
            if (versionString.contains("-beta") || versionString.contains("-alpha")) {
                ReleaseType.BETA
            } else {
                ReleaseType.STABLE
            }
        )
        modLoaders.add("paper")

        modrinth {
            accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))
            projectId.set("EQW4ZtSp")
            minecraftVersions.add(minecraftVersion)
            displayName.set("Towncraft v${project.version}")
        }
    }

    jar {
        enabled = false
    }
    runServer {
        minecraftVersion(minecraftVersion)
        runDirectory.set(rootProject.layout.projectDirectory.dir("run/paper"))
    }
    paperPluginYaml {
        name.set("Towncraft")
        main.set("me.webhead1104.towncraft.platform.paper.TowncraftPaper")
        apiVersion.set("1.21")
        author.set("Webhead1104")
        description.set("A remake of the game Township by Playrix in Minecraft")
        loader.set("me.webhead1104.towncraft.utils.GeneratedClassloader")
        website.set("https://github.com/Webhead1104/Towncraft")
    }

    shadowJar {
        archiveFileName.set("Towncraft-Paper-${project.version}.jar")
        archiveClassifier.set("")
        mergeServiceFiles()

        // Exclude all api dependencies except configurate and IF
        val commonApiDeps = project(":platforms:common")
            .configurations.getByName("api")
            .dependencies
            .filterIsInstance<ModuleDependency>()

        dependencies {
            commonApiDeps
                .filter { it.group != "org.spongepowered" && it.group != "me.devnatan" }
                .forEach { dep ->
                    exclude(dependency("${dep.group}:${dep.name}:.*"))
                }
        }

        relocate("org.spongepowered.configurate", "me.webhead1104.towncraft.libs.configurate")
        relocate("io.leangen.geantyref", "me.webhead1104.towncraft.libs.configurate")
        relocate("com.google.gson", "me.webhead1104.towncraft.libs.configurate")
        relocate("org.bstats", "me.webhead1104.towncraft.libs.bstats")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}