plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.2"
    id("net.cytonic.run-cytosis") version "1.0"
    id("net.kyori.blossom") version "2.2.0"
}

version = project.findProperty("plugin_version") as String? ?: "unknown"

repositories {
    maven("https://jitpack.io")
    maven("https://repo.foxikle.dev/cytonic")
}

dependencies {
    compileOnly(project(":platforms:common"))
    implementation(project(":platforms:common")) {
        isTransitive = false
    }
    implementation(libs.zapper)

    compileOnly(variantOf(libs.cytosis) {
        classifier("all")
    }) {
        exclude("me.devnatan")
    }

    compileOnly(libs.lamp.minestom)
    annotationProcessor(libs.autoService.processor)

    implementation(libs.configurate.gson)
    implementation(libs.configurate.yaml)
    implementation(libs.inventoryFramework.api)
    implementation(libs.inventoryFramework.platform)
    implementation(libs.commonsText)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
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
                        .filter { it.group != "net.cytonic" && it.group != "me.webhead1104" }

                    (commonDeps + platformDeps).map { "${it.group}:${it.name}:${it.version}" }
                }

                val depsString: String = deps.get().joinToString(separator = "\n        ") {
                    """manager.dependency("${it.replace(".", "$")}".replace("$", "."));"""
                }.trimMargin()
                properties.put("depString", depsString)
            }
        }
    }
}

tasks {
    runCytosis {
        cytosisVersion(libs.versions.cytosis.get())
        runDirectory.set(rootProject.layout.projectDirectory.dir("run/cytosis"))
    }
    assemble {
        dependsOn(shadowJar)
    }
    jar {
        enabled = false
    }
    shadowJar {
        archiveFileName.set("Towncraft-Cytosis-${project.version}.jar")
        archiveClassifier.set("")
        mergeServiceFiles()

//        // Exclude all api dependencies except configurate and IF
        val commonApiDeps = project(":platforms:common")
            .configurations.getByName("api")
            .dependencies
            .filterIsInstance<ModuleDependency>()

        dependencies {
            commonApiDeps
                .filter { it.group != "net.cytonic" && it.group != "org.spongepowered" && it.group != "me.devnatan" }
                .forEach { dep -> exclude(dependency("${dep.group}:${dep.name}:.*")) }
        }

        relocate("revxrsal.zapper", "me.webhead1104.towncraft.libs.zapper")

        relocate("org.spongepowered.configurate", "me.webhead1104.towncraft.libs.configurate")
        relocate("io.leangen.geantyref", "me.webhead1104.towncraft.libs.configurate")
        relocate("com.google.gson", "me.webhead1104.towncraft.libs.configurate")
        relocate("me.devnatan.inventoryframework", "me.webhead1104.towncraft.libs.inventoryframework")
        relocate("org.apache.commons.commons-text", "me.webhead1104.towncraft.libs.apache")
    }
}